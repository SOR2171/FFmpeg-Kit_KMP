import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.vanniktech.maven.publish") version "0.37.0"
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

val githubRepo = "https://github.com/SOR2171/FFmpeg-Kit_KMP"
val githubRepoGit = "github.com/sor2171/FFmpeg-Kit_KMP.git"
val groupId = "io.github.sor2171"
val packageName = "ffmpeg-kit-kmp"
val packageVersion = findProperty("VERSION")?.toString() ?: "dev"

group = groupId
version = packageVersion

base {
    archivesName.set(packageName)
}

kotlin {
//    listOf(
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "Shared"
//            isStatic = true
//        }
//    }

    jvm()

    android {
        namespace = "io.github.sor2171.ffmpegkitkmp"
        compileSdk = 37
        minSdk = 28

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            // https://github.com/akashskypatel/ffmpeg-kit-builders
            implementation(project(":ffmpeg-aar-wrapper"))

            implementation("net.java.dev.jna:jna:5.19.1@aar")
            implementation(libs.androidx.espresso.core)
        }
        commonMain.dependencies {
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(libs.jna)
            implementation(libs.slf4j.simple)
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = groupId,
        artifactId = packageName,
        version = packageVersion
    )

    pom {
        name.set("FFmpegKitKmp")

        description.set(
            "FFmpegKitKmp is a FFmpeg runner for Android and all JVM. " +
                    "The Kotlin code is MIT licensed; bundled FFmpeg / FFmpeg Kit " +
                    "components are licensed under LGPL-3.0 or GPL-3.0."
        )

        inceptionYear.set("2026")

        url.set(githubRepo)

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("repo")
                comments.set("Applies to the Kotlin code in this project.")
            }
            license {
                name.set("GPL-3.0")
                url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                distribution.set("repo")
                comments.set(
                    "Applies to FFmpeg components."
                )
            }
        }

        developers {
            developer {
                id.set("sor2171")
                name.set("SOR")
                email.set("sor2171@foxmail.com")
            }
        }

        scm {
            connection.set("scm:git:https://$githubRepoGit")
            developerConnection.set("scm:git:ssh://$githubRepoGit")
            url.set(githubRepo)
        }
    }
}