plugins {
    id("multiplatform-setup")
    id("multiplatform-android-setup")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.JetBrains.KotlinX.Coroutines.core)
                implementation(Deps.JetBrains.KotlinX.Serialization.core)
                implementation(Deps.JetBrains.Ktor.clientCore)
                implementation(Deps.JetBrains.Ktor.clientJson)
                implementation(Deps.JetBrains.Ktor.clientLogging)
                implementation(Deps.JetBrains.Ktor.clientSerialization)
                implementation(project(":common:utils"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.JetBrains.Ktor.clientOkhttp)
                implementation(Deps.Slf4j.simple)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(Deps.JetBrains.Ktor.clientOkhttp)
                implementation(Deps.Slf4j.simple)
            }
        }
    }
}
