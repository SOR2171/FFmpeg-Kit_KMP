package io.github.sor2171.ffmpegkitkmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "FunctionName")
expect interface FFmpegKitCLib {
    companion object {
        val INSTANCE: FFmpegKitCLib
    }
    fun ffmpeg_kit_execute(command: String): Long
    fun ffmpeg_kit_session_get_state(sessionId: Long): Int
    fun ffmpeg_kit_session_get_logs_as_string(sessionId: Long): String?
    fun ffmpeg_kit_close_session(sessionId: Long)

    fun ffprobe_kit_execute(command: String): Long
    fun ffprobe_kit_close_session(sessionId: Long)
}