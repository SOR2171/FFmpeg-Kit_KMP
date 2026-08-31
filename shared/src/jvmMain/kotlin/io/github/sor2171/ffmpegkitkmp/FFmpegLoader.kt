package io.github.sor2171.ffmpegkitkmp

import io.github.sor2171.ffmpegkitkmp.Platform.Os
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileLock
import java.security.MessageDigest

object NativeLibraryLoader {

    private var loadedFilePath: String? = null
    private val loadLock = Any()
    private var isLoaded = false

    @Suppress("UnsafeDynamicallyLoadedCode")
    @Synchronized
    fun loadFFmpeg(): String {
        // 1. 如果当前 JVM 已加载，直接返回
        if (isLoaded) {
            return loadedFilePath ?: throw IllegalStateException("已加载但路径丢失")
        }

        val (resourcePath, suffix) = getPlatformLibraryInfo()

        // 2. 计算资源内容的哈希（用于跨进程缓存）
        val hash = computeResourceHash(resourcePath)
        val tempDir = System.getProperty("java.io.tmpdir")
        val tempFile = File(tempDir, "ffmpegkit_$hash$suffix")

        // 3. 跨进程写入锁：防止两个 JVM 同时写入同一个临时文件
        val lockFile = File(tempDir, "ffmpegkit_$hash.lock")
        lockFile.parentFile?.mkdirs()

        RandomAccessFile(lockFile, "rw").use { raf ->
            val lock: FileLock = raf.channel.lock() // 排他锁，阻塞等待
            try {
                if (!tempFile.exists()) {
                    // 只有持有锁且文件不存在时才写入
                    val inputStream = getResourceStream(resourcePath)
                        ?: throw IllegalStateException("资源未找到: $resourcePath")

                    inputStream.use { input ->
                        tempFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    // 确保文件完整写入并刷新到磁盘
                    tempFile.outputStream().use { it.flush() }
                }
            } finally {
                lock.release()
            }
        }
        // 锁文件此时已关闭，其他 JVM 可继续

        // 4. JVM 内加载锁：防止同一个 JVM 内多个类加载器重复加载
        synchronized(loadLock) {
            if (!isLoaded) {
                System.load(tempFile.absolutePath)
                isLoaded = true
                loadedFilePath = tempFile.absolutePath
            }
        }

        return tempFile.absolutePath
    }

    private fun computeResourceHash(resourcePath: String): String {
        val inputStream = getResourceStream(resourcePath)
            ?: throw IllegalStateException("资源不存在: $resourcePath")

        return inputStream.use { stream ->
            val digest = MessageDigest.getInstance("SHA-256")
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (stream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
            digest.digest().joinToString("") { "%02x".format(it) }.take(16)
        }
    }

    // 辅助：获取资源流（适配 JAR 包和不同类加载器）
    private fun getResourceStream(resourcePath: String): java.io.InputStream? {
        // 优先使用当前线程上下文类加载器（适配 Web 容器、Spring Boot）
        return Thread.currentThread().contextClassLoader?.getResourceAsStream(resourcePath)
            ?: javaClass.getResourceAsStream(resourcePath)
            ?: ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath)
    }

    private fun getPlatformLibraryInfo(): Pair<String, String> {
        val platform = currentPlatform()

        return when (platform.os) {
            Os.Windows if platform.architecture == Platform.Architecture.X86_64
                -> "/natives/windows-x86_64/libffmpegkit.dll" to ".dll"

            Os.MacOS if platform.architecture == Platform.Architecture.Arm64
                -> "/natives/macos-universal/ffmpegkit.dylib" to ".dylib"

            Os.Linux if platform.architecture == Platform.Architecture.X86_64
                -> "/natives/linux-x86_64/libffmpegkit.so" to ".so"

            else -> throw UnsupportedOperationException("Unsupported OS: ${platform.os}, architecture: ${platform.architecture}.")
        }
    }
}