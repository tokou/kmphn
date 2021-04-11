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
        commonMain {
            dependencies {
                implementation(Deps.ArkIvanov.Decompose.decompose)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlin)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlinExtensionsCoroutines)
                implementation(Deps.JetBrains.KotlinX.Coroutines.core)
                implementation(Deps.JetBrains.KotlinX.Serialization.core)
                implementation(Deps.JetBrains.KotlinX.Serialization.json)
                implementation(Deps.JetBrains.Ktor.clientCore)
                implementation(Deps.JetBrains.Ktor.clientJson)
                implementation(Deps.JetBrains.Ktor.clientSerialization)
                implementation(Deps.JetBrains.Ktor.clientLogging)
                implementation(project(":common:utils"))
            }
        }
        androidMain {
            dependencies {
                implementation(Deps.Squareup.SqlDelight.androidDriver)
                implementation(Deps.Squareup.SqlDelight.sqliteDriver)
            }
        }
        desktopMain {
            dependencies {
                implementation(Deps.Squareup.SqlDelight.sqliteDriver)
            }
        }
        iosMain {
            dependencies {
                implementation(Deps.Squareup.SqlDelight.nativeDriver)
            }
        }
        webMain {
            dependencies {
                //implementation(Deps.Squareup.SqlDelight.sqljsDriver)
            }
        }
    }
}
