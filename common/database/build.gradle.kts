plugins {
    id("multiplatform-setup")
    id("multiplatform-android-setup")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("NewsDatabase") {
        packageName = "com.github.tokou.common.database"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.ArkIvanov.Decompose.decompose)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlin)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlinExtensionsCoroutines)
                implementation(Deps.JetBrains.KotlinX.Coroutines.core)
                implementation(Deps.JetBrains.KotlinX.Serialization.core)
                implementation(Deps.JetBrains.Ktor.clientCore)
                implementation(Deps.JetBrains.Ktor.clientJson)
                implementation(Deps.JetBrains.Ktor.clientSerialization)
                implementation(Deps.JetBrains.Ktor.clientLogging)
                implementation(project(":common:utils"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Squareup.SqlDelight.androidDriver)
                implementation(Deps.Squareup.SqlDelight.sqliteDriver)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(Deps.Squareup.SqlDelight.sqliteDriver)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Deps.Squareup.SqlDelight.nativeDriver)
            }
        }
    }
}
