import org.jetbrains.compose.compose

plugins {
    id("org.jetbrains.compose") version "0.4.0-build174"
    id("com.android.application")
    kotlin("android")
}

group = "com.github.tokou"
version = "1.0.0"

repositories {
    google()
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.3.0-alpha04")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.github.tokou.android"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
