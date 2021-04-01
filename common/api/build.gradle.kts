plugins {
    id("multiplatform-setup")
    id("multiplatform-android-setup")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.JetBrains.KotlinX.Coroutines.core)
                implementation(Deps.JetBrains.KotlinX.Serialization.core)
                implementation(Deps.JetBrains.KotlinX.Serialization.json)
                implementation(Deps.JetBrains.Ktor.clientCore)
                implementation(Deps.JetBrains.Ktor.clientJson)
                implementation(Deps.JetBrains.Ktor.clientLogging)
                implementation(Deps.JetBrains.Ktor.clientSerialization)
                implementation(project(":common:utils"))
            }
        }
        androidMain {
            dependencies {
                implementation(Deps.JetBrains.Ktor.clientOkhttp)
                implementation(Deps.Slf4j.simple)
            }
        }
        desktopMain {
            dependencies {
                implementation(Deps.JetBrains.Ktor.clientOkhttp)
                implementation(Deps.Slf4j.simple)
            }
        }
        iosMain {
            dependencies {
                implementation(Deps.JetBrains.Ktor.clientIos)
            }
        }
    }
}
