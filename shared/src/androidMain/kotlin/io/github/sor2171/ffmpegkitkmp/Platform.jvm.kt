package io.github.sor2171.ffmpegkitkmp

import android.os.Build
import io.github.sor2171.ffmpegkitkmp.Platform.Os

actual fun currentPlatform(): Platform {
    val archName = Build.SUPPORTED_ABIS.firstOrNull() ?: Build.CPU_ABI ?: "unknown"
    val architecture = when {
        archName.startsWith("x86_64") -> Platform.Architecture.X86_64
        archName.startsWith("x86") -> Platform.Architecture.X86
        archName.startsWith("arm64-v8a") || archName.startsWith("aarch64") -> Platform.Architecture.Arm64
//        archName.startsWith("armeabi-v7a") || archName.startsWith("arm") -> Platform.Architecture.Arm32
        else -> error("Unsupported architecture: $archName")
    }
    return Platform(
        os = Os.Android,
        architecture = architecture
    )
}