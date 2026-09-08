package io.github.sor2171.ffmpegkitkmp

import com.sun.jna.Library
import com.sun.jna.Native
import org.slf4j.LoggerFactory

@Suppress("FunctionName")
internal interface FFmpegKitCLib : Library {
    fun ffmpeg_kit_execute(command: String): Long
    fun ffmpeg_kit_session_get_state(sessionId: Long): Int
    fun ffmpeg_kit_session_get_logs_as_string(sessionId: Long): String?
    fun ffmpeg_kit_close_session(sessionId: Long)

    fun ffprobe_kit_execute(command: String): Long
    fun ffprobe_kit_close_session(sessionId: Long)
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object FFmpegRunner {
    val log = LoggerFactory.getLogger(this.javaClass)!!

    private val cLib: FFmpegKitCLib by lazy {
        Native.load("ffmpegkit", FFmpegKitCLib::class.java)
    }

    actual fun execute(vararg cmd: String): String? {
        return try {
            val command = StringBuilder()
            cmd.forEach { command.append(it).append(" ") }
            val sessionId = cLib.ffmpeg_kit_execute(command.toString())

            val logs = cLib.ffmpeg_kit_session_get_logs_as_string(sessionId)
            val state = cLib.ffmpeg_kit_session_get_state(sessionId)

            cLib.ffmpeg_kit_close_session(sessionId)

            if (state == 2) logs else null
        } catch (e: Throwable) {
            log.error("FFmpegRunner error: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    actual fun ffprobe(vararg cmd: String): String? {
        return try {
            val command = cmd.joinToString(" ")
            val sessionId = cLib.ffprobe_kit_execute(command)
            if (sessionId == 0L) return null

            val logs = cLib.ffmpeg_kit_session_get_logs_as_string(sessionId)

            cLib.ffprobe_kit_close_session(sessionId)
            logs
        } catch (e: Throwable) {
            log.error("FFprobe error", e)
            null
        }
    }
}