package io.github.sor2171.ffmpegkitkmp

import io.github.sor2171.ffmpegkitkmp.Platform.Os
import java.io.File

object NativeLibraryLoader {

    private var loadedFile: File? = null

    @Suppress("UnsafeDynamicallyLoadedCode")
    @Synchronized
    fun loadFFmpeg(): String {
        // 如果已经加载过了，直接返回已提取的绝对路径
        loadedFile?.let { return it.absolutePath }

        val (resourcePath, suffix) = getPlatformLibraryInfo()

        // 1. 从 classpath 中提取资源流
        val inputStream = object {}.javaClass.getResourceAsStream(resourcePath)
            ?: throw IllegalStateException("未在资源路径中找到文件: $resourcePath。请检查 resources 目录布局！")

        // 2. 将动态库写入临时文件
        val tempFile = File.createTempFile("ffmpegkit_", suffix).apply {
            deleteOnExit()
        }

        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // 3. 使用 System.load 绝对路径加载到 JVM 进程内存
        System.load(tempFile.absolutePath)

        loadedFile = tempFile
        return tempFile.absolutePath
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