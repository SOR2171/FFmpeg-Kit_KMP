package io.github.sor2171.ffmpegkitkmp

import com.sun.jna.Callback
import com.sun.jna.Library
import com.sun.jna.Native
import org.slf4j.LoggerFactory

internal fun interface FFmpegLogCallback : Callback {
    fun invoke(sessionId: Long, level: Int, logText: String?)
}

@Suppress("FunctionName")
internal interface FFmpegKitCLib : Library {
    fun ffmpeg_kit_execute(command: String): Long
    fun ffmpeg_kit_session_get_state(sessionId: Long): Int
    fun ffmpeg_kit_session_get_logs_as_string(sessionId: Long): String?
    fun ffmpeg_kit_close_session(sessionId: Long)
    fun ffmpeg_kit_set_log_callback(callback: FFmpegLogCallback)
    fun ffmpeg_kit_config_set_log_level(level: Int)
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object FFmpegRunner {
    val log = LoggerFactory.getLogger(this.javaClass)!!

    private val cLib: FFmpegKitCLib by lazy {
        val libAbsolutePath = NativeLibraryLoader.loadFFmpeg()
        val instance = Native.load(libAbsolutePath, FFmpegKitCLib::class.java)

        try {
            instance.ffmpeg_kit_config_set_log_level(32)
            instance.ffmpeg_kit_set_log_callback { sessionId, level, logText ->
                if (!logText.isNullOrEmpty()) {val message = "[FFmpeg Log][$sessionId]: $logText"
                    when {
                        level <= 16 -> log.error(message)  // ERROR 及以下 (FATAL, PANIC)
                        level <= 24 -> log.warn(message)   // WARNING
                        level <= 32 -> log.info(message)   // INFO
                        else -> log.debug(message)         // VERBOSE, DEBUG, TRACE
                    }
                }
            }
        } catch (e: Throwable) {
            log.error("subscribe FFmpeg log failed: ${e.message}")
        }

        instance
    }

    actual fun execute(vararg cmd: String): Int {
        return try {
            val command = StringBuilder()
            cmd.forEach { command.append(it).append(" ") }
            val sessionId = cLib.ffmpeg_kit_execute(command.toString())

            val logs = cLib.ffmpeg_kit_session_get_logs_as_string(sessionId)
            if (!logs.isNullOrEmpty()) {
                log.info("FFmpegKit log:")
                log.info(logs)
                log.info("FFmpegKit log ends")
            }

            // 获取 Session 状态 (2 代表 SessionState.COMPLETED)
            val state = cLib.ffmpeg_kit_session_get_state(sessionId)

            cLib.ffmpeg_kit_close_session(sessionId)

            // 如果状态为 COMPLETED (2) 则返回 0，否则返回 -1
            if (state == 2) 0 else -1
        } catch (e: Throwable) {
            log.error("FFmpegRunner error: ${e.message}")
            e.printStackTrace()
            -1
        }
    }
}