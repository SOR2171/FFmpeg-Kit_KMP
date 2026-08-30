package io.github.sor2171.ffmpegkitkmp

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FFmpegTestForAndroid {

    private fun runCommand(command: String) {
        println("$ ffmpeg $command")

        val code = FFmpegRunner.execute(command)
        println("Code = $code")
    }

    @Test
    fun testFFmpegVersion() {
        println("OS: ${System.getProperty("os.name")}")
        println("Arch: ${System.getProperty("os.arch")}")

        runCommand("-version")
    }

    @Test
    fun testFFmpegCreateColorBar() {
        val command = "-re -f lavfi -i testsrc=duration=10:size=1920x1080:rate=60 -f null -"
        runCommand(command)
    }
}
