import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose") version "0.4.0-build174"
    kotlin("multiplatform")
    id("com.android.library")
}

group = "com.github.tokou"
version = "1.0.0"

repositories {
    google()
}

android {
    // Workaround for https://youtrack.jetbrains.com/issue/KT-43944
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
    compileSdkVersion(30)
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        }
    }
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }
}
dependencies {
    implementation("androidx.compose.runtime:runtime:1.0.0-beta02")
}

kotlin {
    android()
    jvm("desktop")
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.materialIconsExtended)
                api(compose.animation)
                api(compose.ui)
                implementation("com.arkivanov.decompose:decompose:0.1.9")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.1.9")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
                implementation(project(":common:main"))
                implementation(project(":common:detail"))
                implementation(project(":common:root"))
            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.2.0")
                api("androidx.core:core-ktx:1.3.2")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.desktop.common)
            }
        }
        val desktopTest by getting
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs.plus("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}
