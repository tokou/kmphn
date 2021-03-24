import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.30"
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
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }
}

kotlin {
    android()
    jvm("desktop")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.1.0")
                implementation("io.ktor:ktor-client-core:1.5.2")
                implementation("io.ktor:ktor-client-json:1.5.2")
                implementation("io.ktor:ktor-client-logging:1.5.2")
                implementation("io.ktor:ktor-client-serialization:1.5.2")
                implementation(project(":common:utils"))
            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:1.5.2")
                implementation("org.slf4j:slf4j-simple:1.7.30")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:1.5.2")
                implementation("org.slf4j:slf4j-simple:1.7.30")
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
