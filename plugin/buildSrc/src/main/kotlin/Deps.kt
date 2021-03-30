import org.gradle.kotlin.dsl.version

object Deps {

    object JetBrains {
        object Kotlin {
            private const val VERSION = "1.4.31"
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
            const val testCommon = "org.jetbrains.kotlin:kotlin-test-common:$VERSION"
            const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:$VERSION"
            const val testAnnotationsCommon = "org.jetbrains.kotlin:kotlin-test-annotations-common:$VERSION"
        }

        object KotlinX {
            object Coroutines {
                private const val VERSION = "1.4.3"
                const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION"
                const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$VERSION"
                const val swing = "org.jetbrains.kotlinx:kotlinx-coroutines-swing:$VERSION"
            }

            object DateTime {
                const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.1.1"
            }

            object Serialization {
                const val gradlePlugin = "org.jetbrains.kotlin:kotlin-serialization:1.4.30"
                private const val VERSION = "1.1.0"
                const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$VERSION"
                const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$VERSION"
            }
        }

        object Compose {
            private const val VERSION = "0.4.0-build178"
            const val gradlePlugin = "org.jetbrains.compose:compose-gradle-plugin:$VERSION"

            object Runtime {
                const val runtime = "androidx.compose.runtime:runtime:1.0.0-beta02"
            }
        }

        object Ktor {
            private const val VERSION = "1.5.2"
            const val clientCore = "io.ktor:ktor-client-core:$VERSION"
            const val clientJson = "io.ktor:ktor-client-json:$VERSION"
            const val clientLogging = "io.ktor:ktor-client-logging:$VERSION"
            const val clientSerialization = "io.ktor:ktor-client-serialization:$VERSION"
            const val clientOkhttp = "io.ktor:ktor-client-okhttp:$VERSION"
        }
    }

    object Android {
        object Tools {
            object Build {
                const val gradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha12"
            }
            const val desugarJdk = "com.android.tools:desugar_jdk_libs:1.1.5"
        }
    }

    object AndroidX {
        object AppCompat {
            const val appCompat = "androidx.appcompat:appcompat:1.3.0-beta01"
            const val browser = "androidx.browser:browser:1.3.0"
        }

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha05"
        }
    }

    object ArkIvanov {
        object MviKotlin {
            private const val VERSION = "2.0.1"
            const val mvikotlin = "com.arkivanov.mvikotlin:mvikotlin:$VERSION"
            const val mvikotlinMain = "com.arkivanov.mvikotlin:mvikotlin-main:$VERSION"
            const val mvikotlinMainIosX64 = "com.arkivanov.mvikotlin:mvikotlin-main-iosx64:$VERSION"
            const val mvikotlinMainIosArm64 = "com.arkivanov.mvikotlin:mvikotlin-main-iosarm64:$VERSION"
            const val mvikotlinLogging = "com.arkivanov.mvikotlin:mvikotlin-logging:$VERSION"
            const val mvikotlinTimeTravel = "com.arkivanov.mvikotlin:mvikotlin-timetravel:$VERSION"
            const val mvikotlinExtensionsCoroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$VERSION"
        }

        object Decompose {
            private const val VERSION = "0.1.9"
            const val decompose = "com.arkivanov.decompose:decompose:$VERSION"
            const val decomposeIosX64 = "com.arkivanov.decompose:decompose-iosx64:$VERSION"
            const val decomposeIosArm64 = "com.arkivanov.decompose:decompose-iosarm64:$VERSION"
            const val extensionsCompose = "com.arkivanov.decompose:extensions-compose-jetbrains:$VERSION"
        }
    }

    object Squareup {
        object SqlDelight {
            private const val VERSION = "1.4.4"
            const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$VERSION"
            const val androidDriver = "com.squareup.sqldelight:android-driver:$VERSION"
            const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:$VERSION"
            const val nativeDriver = "com.squareup.sqldelight:native-driver:$VERSION"
        }
    }

    object CashApp {
        object Turbine {
            const val turbine = "app.cash.turbine:turbine:0.4.1"
        }
    }

    object TouchLab {
        object Kermit {
            const val kermit = "co.touchlab:kermit:0.1.7"
        }
    }

    object Slf4j {
        const val simple = "org.slf4j:slf4j-simple:1.7.30"
    }

    object OpenBakery {
        const val xcodePlugin = "gradle.plugin.org.openbakery:plugin:0.20.1"
    }
}
