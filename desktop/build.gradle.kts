import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // kotlin("jvm") doesn't work well in IDEA/AndroidStudio
    // https://github.com/JetBrains/compose-jb/issues/22
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

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
                implementation(project(":common:api"))
                implementation(project(":common:database"))
                implementation(project(":common:root"))
                implementation(project(":common:utils"))
                implementation(compose.desktop.currentOs)
                implementation(Deps.ArkIvanov.Decompose.decompose)
                implementation(Deps.ArkIvanov.Decompose.extensionsCompose)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlin)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlinMain)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlinLogging)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlinExtensionsCoroutines)
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
            packageName = "HN"
            packageVersion = "1.0.0"

            modules("java.sql")

            macOS {
                iconFile.set(project.file("src/jvmMain/resources/icon.icns"))
                bundleID = "com.github.tokou.hn.macos"
                packageName = "HN"
            }

            linux {
                iconFile.set(project.file("src/jvmMain/resources/icon.png"))
            }

            windows {
                iconFile.set(project.file("src/jvmMain/resources/icon.ico"))
                menuGroup = "HN"
                upgradeUuid = "D11B837D-91C3-4230-AC04-D73C0CAD719D"
                dirChooser = true
            }
        }
    }
}
