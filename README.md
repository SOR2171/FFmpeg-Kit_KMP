# FFmpeg Kit KMP

This is a Kotlin Multiplatform lib project targeting Android, Desktop (JVM).

![](https://img.shields.io/badge/Linux-FCC624?style=&logo=linux&logoColor=black)
![](https://img.shields.io/badge/Windows-10/11-2376bc?style=flat&logo=windows&logoColor=ffffff)
![](https://img.shields.io/badge/MacOS-333?style=flat&logo=apple&logoColor=ffffff)
![](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)

![](https://img.shields.io/github/license/sor2171/Audio-Detection-Tool_FFmpegKit.svg)
![](https://img.shields.io/badge/Kotlin_Multiplatform-7F52FF?style=flat&logo=kotlin&logoColor=white)

## Use With Gradle

```kotlin
commonMain.dependencies {
    implementation("io.github.sor2171:ffmpeg-kit-kmp:0.11.2")
}
```

Run command like this:
```kotlin
fun runCommand(vararg command: String) {
    println("$ ffmpeg ${command.contentToString()}")

    val code = FFmpegRunner.execute(*command)
    println("Code = $code")
    assert(code == 0)
}

fun testFFmpegCreateColorBar() {
    runCommand(
        "-re",
        "-f lavfi",
        "-i testsrc=duration=10:size=1920x1080:rate=60",
        "-f null -"
    )
}
```

If you encounter with this error:

```bash
C:\Users\32721\AppData\Local\Temp\ffmpegkit_2621802206663535111.dll: %1 不是有效的 Win32 应用程序。
	at java.base/jdk.internal.loader.NativeLibraries.load(Native Method)
	......
```

Please compile the jars yourself and get them in [`./shared/build/libs`](./shared/build/libs).

## About FFmpeg

I use the release from [FFmpeg Kit](https://github.com/akashskypatel/ffmpeg-kit-builders) (release v0.11.0),
by [akashskypatel](https://github.com/akashskypatel), with license of LGPL-3.0 license.

## About Supported Platforms

|         | X86 | X64 | Arm32 | Arm64 |
|---------|:---:|:---:|:-----:|:-----:|
| Android |     |  ✅  |   ✅   |   ✅   |
| IOS     |     |     |       |       |
| Windows |     |  ✅  |       |       |
| Linux   |     |  ✅  |       |       |
| MacOS   |     |     |       |   ✅   |

I can't test the macOS and IOS version. So, here is no release for IOS. Welcome for your PR.

The version of IOS will come, but not soon.