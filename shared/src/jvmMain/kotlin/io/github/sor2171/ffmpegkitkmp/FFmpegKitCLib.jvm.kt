package io.github.sor2171.ffmpegkitkmp

import com.sun.jna.Library
import com.sun.jna.Native

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "FunctionName")
actual interface FFmpegKitCLib : Library {
    actual companion object {
        actual val INSTANCE: FFmpegKitCLib by lazy {
            System.setProperty("jna.encoding", "UTF-8")
            Native.load(
                "ffmpegkit",
                FFmpegKitCLib::class.java,
                mapOf(Library.OPTION_STRING_ENCODING to "UTF-8")
            )
        }
    }

    actual fun ffmpeg_kit_execute(command: String): Long
    actual fun ffmpeg_kit_session_get_state(sessionId: Long): Int
    actual fun ffmpeg_kit_session_get_logs_as_string(sessionId: Long): String?
    actual fun ffmpeg_kit_close_session(sessionId: Long)

    actual fun ffprobe_kit_execute(command: String): Long
    actual fun ffprobe_kit_close_session(sessionId: Long)
}