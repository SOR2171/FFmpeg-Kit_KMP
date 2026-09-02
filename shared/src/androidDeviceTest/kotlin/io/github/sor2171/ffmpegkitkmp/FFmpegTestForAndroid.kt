package io.github.sor2171.ffmpegkitkmp

import org.junit.Test

class FFmpegTestForAndroid {

    private fun runCommand(vararg command: String) {
        println("$ ffmpeg ${command.contentToString()}")

        val code = FFmpegRunner.execute(*command)
        println("Code = $code")
        assert(code == 0)
    }

    @Test
    fun testFFmpegVersion() {
        println("OS: ${System.getProperty("os.name")}")
        println("Arch: ${System.getProperty("os.arch")}")

        runCommand("-version")
    }

    @Test
    fun testFFmpegCreateColorBar() {
        runCommand(
            "-re",
            "-f lavfi",
            "-i testsrc=duration=10:size=1920x1080:rate=60",
            "-f null -"
        )
    }
}