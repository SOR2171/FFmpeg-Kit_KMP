package io.github.sor2171.ffmpegkitkmp

data class Platform(
    val os: Os,
    val architecture: Architecture
) {
    enum class Os {
        Windows,
        Linux,
        MacOS,
        Android
    }

    enum class Architecture {
        X86,
        X86_64,
        Arm64,
        Arm32
    }
}

expect fun currentPlatform(): Platform