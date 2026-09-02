package io.github.sor2171.ffmpegkitkmp

import android.util.Log
import com.sun.jna.Callback
import com.sun.jna.Library
import com.sun.jna.Native
import kotlin.collections.forEach

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
    val logTag = "FFmpegRunner"

    private val cLib: FFmpegKitCLib by lazy {
        // Android 系统会自动从 apk/lib/<abi>/ 中查找 libffmpegkit.so
        val instance = Native.load("ffmpegkit", FFmpegKitCLib::class.java)

        try {
            instance.ffmpeg_kit_config_set_log_level(32)
            instance.ffmpeg_kit_set_log_callback { sessionId, _, logText ->
                if (!logText.isNullOrEmpty()) {
                    Log.i(logTag, "[FFmpeg Log][$sessionId]: $logText")
                }
            }
        } catch (e: Throwable) {
            Log.e(logTag, "subscribe FFmpeg log failed: ${e.message}", e)
        }

        instance
    }

    actual fun execute(vararg cmd: String): Int {
        return try {
            val command = StringBuilder()
            cmd.forEach { command.append(it).append(" ") }
            val sessionId = cLib.ffmpeg_kit_execute(command.toString())

            // 先尝试获取 session 完整日志
            var logs = cLib.ffmpeg_kit_session_get_logs_as_string(sessionId)

            // 如果 logs 为空，回退获取控制台原始 output (如 -version 的输出)
            if (logs.isNullOrEmpty()) {
                logs = cLib.ffmpeg_kit_session_get_logs_as_string(sessionId)
            }

            if (!logs.isNullOrEmpty()) {
                Log.i(logTag, "FFmpegKit log:\n$logs")
            } else {
                Log.w(logTag, "FFmpegKit can't get logs!")
            }

            val state = cLib.ffmpeg_kit_session_get_state(sessionId)
            cLib.ffmpeg_kit_close_session(sessionId)

            if (state == 2) 0 else -1
        } catch (e: Throwable) {
            Log.e(logTag, "FFmpegRunner Android error:", e)
            -1
        }
    }
}
