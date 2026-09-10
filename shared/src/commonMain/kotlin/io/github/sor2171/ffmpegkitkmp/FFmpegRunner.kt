package io.github.sor2171.ffmpegkitkmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
object FFmpegRunner {
    private val logger = Logger(this::class)
    private val cLib = FFmpegKitCLib.INSTANCE

    /**
     * execute FFmpeg command line
     * @param cmd such as "-i input.mp3", "-f s16le", "output.pcm"
     * @return command line output
     */
    fun execute(vararg cmd: String): String? {
        return try {
            val command = cmd.joinToString(" ")
            logger.info("$ ffmpeg $command")
            val sessionId = cLib.ffmpeg_kit_execute(command)

            val logs = cLib.ffmpeg_kit_session_get_logs_as_string(sessionId)
            val state = cLib.ffmpeg_kit_session_get_state(sessionId)

            cLib.ffmpeg_kit_close_session(sessionId)

            if (state == 2) logs else null
        } catch (e: Throwable) {
            logger.error("FFmpegRunner error: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    /**
     * execute FFprobe command line
     * @param cmd such as
     *             "-v quiet",
     *             "-print_format json",
     *             "-show_format",
     *             "-show_streams",
     *             "/path/to/your/video"
     * @return command line output
     */
    fun ffprobe(vararg cmd: String): String? {
        return try {
            val command = cmd.joinToString(" ")
            val sessionId = cLib.ffprobe_kit_execute(command)
            if (sessionId == 0L) return null

            val logs = cLib.ffmpeg_kit_session_get_logs_as_string(sessionId)

            cLib.ffprobe_kit_close_session(sessionId)
            logs
        } catch (e: Throwable) {
            logger.error("FFprobe error ${e.message}")
            null
        }
    }
}
