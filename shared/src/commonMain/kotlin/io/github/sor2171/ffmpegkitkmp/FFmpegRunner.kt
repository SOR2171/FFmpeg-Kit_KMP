package io.github.sor2171.ffmpegkitkmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object FFmpegRunner {
    /**
     * 执行 FFmpeg 命令行参数
     * @param cmd 例如 "-i input.mp3 -f s16le output.pcm"
     * @return 返回码 (0 代表成功)
     */
    fun execute(vararg cmd: String): String?
    fun ffprobe(vararg cmd: String): String?
}
