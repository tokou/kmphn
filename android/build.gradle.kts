
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
    implementation(project(":common:compose-ui"))
    implementation(project(":common:database"))
    implementation(project(":common:root"))
    implementation(project(":common:utils"))
    implementation("androidx.activity:activity-compose:1.3.0-alpha04")
    implementation("com.arkivanov.decompose:decompose:0.1.9")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.1.9")
    implementation("com.arkivanov.mvikotlin:mvikotlin:2.0.1")
    implementation("com.arkivanov.mvikotlin:mvikotlin-main:2.0.1")
    implementation("com.arkivanov.mvikotlin:mvikotlin-logging:2.0.1")
    implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:2.0.1")
    implementation(compose.material)
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
