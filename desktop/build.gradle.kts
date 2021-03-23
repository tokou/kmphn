import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.4.0-build174"
}

group = "com.github.tokou"
version = "1.0.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common:compose-ui"))
                implementation(project(":common:root"))
                implementation(project(":common:utils"))
                implementation(compose.desktop.currentOs)
                implementation("com.arkivanov.decompose:decompose:0.1.9")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.1.9")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
        }
    }
}
