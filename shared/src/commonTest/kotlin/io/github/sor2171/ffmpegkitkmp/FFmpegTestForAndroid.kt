package io.github.sor2171.ffmpegkitkmp

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FFmpegTestForAndroid {

    @Test
    fun testFFmpegVersion() {
        Log.i("TestClass", "操作系统: ${System.getProperty("os.name")}")
        Log.i("TestClass", "架构: ${System.getProperty("os.arch")}")

        val command = "-version"
        Log.i("TestClass", "$ ffmpeg $command")

        val code = FFmpegRunner.execute(command)
        Log.i("TestClass", "Code = $code")
    }
}
