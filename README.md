# FFmpeg Kit KMP

This is a Kotlin Multiplatform library project targeting Android, Desktop (JVM).

![Linux](https://img.shields.io/badge/Linux-FCC624?style=&logo=linux&logoColor=black)
![Win](https://img.shields.io/badge/Windows-10/11-2376bc?style=flat&logo=windows&logoColor=ffffff)
![macOS](https://img.shields.io/badge/MacOS-333?style=flat&logo=apple&logoColor=ffffff)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)

![License](https://img.shields.io/badge/license-MIT%20%2B%20LGPL--3.0%20%2F%20GPL--3.0-blue)
![KMP](https://img.shields.io/badge/Kotlin_Multiplatform-7F52FF?style=flat&logo=kotlin&logoColor=white)

## About Supported Platforms

|         | X86 | X64 | Arm32 | Arm64 |
|---------|:---:|:---:|:-----:|:-----:|
| Android |     |  ✅  |   ✅   |   ✅   |
| iOS     |     |     |       |       |
| Windows |     |  ✅  |       |       |
| Linux   |     |  ✅  |       |       |
| MacOS   |     |     |       |   ✅   |

I can't test the macOS and iOS version. Therefore, there is no iOS release.
PRs are welcome.

iOS support is planned, but not soon.

## Use with Gradle

**NOTICE**: This version is subject to GPL-3.0,
namely `bundle-video_hw-shared-gpl-release`.

```kotlin
commonMain.dependencies {
    implementation("io.github.sor2171:ffmpeg-kit-kmp:0.11.4")
}
```

If you want to replace the FFmpeg libraries, they are located in
[`shared/src/jvmMain/resources`](./shared/src/jvmMain/resources) /
[`ffmpeg-aar-wrapper/libs`](./ffmpeg-aar-wrapper/libs).

```bash
./gradlew :shared:assemble
```

The built artifacts will be in [`shared/build/libs`](./shared/build/libs) /
[`shared/build/outputs/aar`](./shared/build/outputs/aar).

To run an FFmpeg command:

```kotlin
fun testFFmpegCreateColorBar() {
    val output = FFmpegRunner.execute(
        "-re",
        "-f lavfi",
        "-i testsrc=duration=10:size=1920x1080:rate=60",
        "-f null -"
    )
    println(output)
}
```

To run an FFprobe command:

```kotlin
fun ffprobe() {
    val json = FFmpegRunner.ffprobe(
        "-v quiet",
        "-print_format json",
        "-show_format",
        "-show_streams",
        "/path/to/your/video"
    )
    println(json)
}
```

## License & Compliance

The Kotlin code in this repository is MIT licensed (see `LICENSE`).

This project uses FFmpeg Kit from
[akashskypatel/ffmpeg-kit-builders](https://github.com/akashskypatel/ffmpeg-kit-builders)
(release v0.11.0). The upstream project is generally licensed under
[LGPL-3.0](https://github.com/akashskypatel/ffmpeg-kit-builders/blob/master/LICENSE),
but the Maven artifact `io.github.sor2171:ffmpeg-kit-kmp:0.11.4` bundles
`bundle-video_hw-shared-gpl-release`, so that artifact is subject to GPL-3.0.

FFmpeg / FFmpeg Kit components may be subject to LGPL-3.0 or GPL-3.0 depending
on the build variant. Full texts are in `LICENSE-LGPL` and `LICENSE-GPL`.

Whether you use the Maven artifact or build a JAR/AAR from this repository,
you must comply with the applicable LGPL/GPL terms when distributing. The MIT
license only applies to the Kotlin code in this repository and does not replace
your own compliance obligations for the bundled FFmpeg components.