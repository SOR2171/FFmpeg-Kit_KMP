package io.github.sor2171.ffmpegkitkmp

import org.junit.Test

class FFmpegTest {

    private fun runCommand(vararg command: String) {
        val output = FFmpegRunner.execute(*command)
        println("output = $output")
        assert(output != null)
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

    @Test
    fun ffprobe() {
        val result = FFmpegRunner.ffprobe(
            "-v quiet",
            "-print_format json",
            "-show_format",
            "-show_streams",
            "D:\\Media\\Blender\\output\\meteor_Miku.mp4"
        )
        println(result)
    }
}