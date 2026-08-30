import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.vanniktech.maven.publish") version "0.37.0"
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

group = "io.github.sor2171"
version = findProperty("VERSION")?.toString() ?: "dev"
val githubRepo = "https://github.com/SOR2171/FFmpeg-Kit_KMP"
val githubRepoGit = "github.com/sor2171/FFmpeg-Kit_KMP.git"

kotlin {
    jvm()

    android {
        namespace = "io.github.sor2171.ffmpegkitkmp.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
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

            //noinspection UseTomlInstead
            implementation("net.java.dev.jna:jna:5.19.1@aar")
            implementation(libs.androidx.junit.ktx)
            implementation(libs.androidx.junit)
            implementation(libs.androidx.espresso.core)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.components.resources)
        }
        jvmMain.dependencies {
            implementation(libs.jna)
            implementation(libs.slf4j.simple)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

mavenPublishing {
    pom {
        name.set("FFmegKitKmp")

        description.set(
            "FFmegKitKmp is a FFmpeg runner for Android and all JVM."
        )

        inceptionYear.set("2026")

        url.set(githubRepo)

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
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