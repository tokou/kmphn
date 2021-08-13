
object Deps {

    object JetBrains {
        object Kotlin {
            private const val VERSION = "1.4.32"
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
            const val testCommon = "org.jetbrains.kotlin:kotlin-test-common:$VERSION"
            const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:$VERSION"
            const val testJs = "org.jetbrains.kotlin:kotlin-test-js:$VERSION"
            const val testAnnotationsCommon = "org.jetbrains.kotlin:kotlin-test-annotations-common:$VERSION"
            const val stdlibJs = "org.jetbrains.kotlin:kotlin-stdlib-js:$VERSION"

            object Wrappers {
                object Styled {
                    private const val VERSION = "5.2.3-pre.153-kotlin-1.4.32"
                    const val styled = "org.jetbrains:kotlin-styled:$VERSION"
                }

                object React {
                    private const val VERSION = "16.13.1-pre.110-kotlin-1.4.0"
                    const val react = "org.jetbrains:kotlin-react:$VERSION"
                    const val reactDom = "org.jetbrains:kotlin-react-dom:$VERSION"

                }

                object Router {
                    private const val VERSION = "5.1.2-pre.110-kotlin-1.4.0"
                    const val reactRouterDom = "org.jetbrains:kotlin-react-router-dom:$VERSION"
                }
            }
        }

        object KotlinX {
            object Coroutines {
                private const val VERSION = "1.4.3-native-mt"
                const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3-native-mt"
                const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$VERSION"
                const val swing = "org.jetbrains.kotlinx:kotlinx-coroutines-swing:$VERSION"
            }

            object DateTime {
                const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.1.1"
            }

            object Html {
                const val htmlJs = "org.jetbrains.kotlinx:kotlinx-html-js:0.7.3"
            }

            object Serialization {
                const val gradlePlugin = "org.jetbrains.kotlin:kotlin-serialization:1.4.30"
                private const val VERSION = "1.1.0"
                const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$VERSION"
                const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$VERSION"
            }
        }

        object Compose {
            private const val VERSION = "0.4.0-build180"
            const val gradlePlugin = "org.jetbrains.compose:compose-gradle-plugin:$VERSION"

            object Runtime {
                const val runtime = "androidx.compose.runtime:runtime:1.0.0-beta04"
            }
        }

        object Ktor {
            private const val VERSION = "1.5.3"
            const val clientCore = "io.ktor:ktor-client-core:$VERSION"
            const val clientJson = "io.ktor:ktor-client-json:$VERSION"
            const val clientLogging = "io.ktor:ktor-client-logging:$VERSION"
            const val clientSerialization = "io.ktor:ktor-client-serialization:$VERSION"
            const val clientOkhttp = "io.ktor:ktor-client-okhttp:$VERSION"
            const val clientIos = "io.ktor:ktor-client-ios:$VERSION"
            const val clientJs = "io.ktor:ktor-client-js:$VERSION"
            const val clientCurl = "io.ktor:ktor-client-curl:$VERSION"
        }
    }

    object Npm {
        object React {
            const val VERSION = "16.13.0"
            const val react = "react"
            const val reactDom = "react-dom"
        }
    }

    object CcFraser {
        object Muirwik {
            private const val VERSION = "0.6.7"
            const val muirwik = "com.ccfraser.muirwik:muirwik-components:$VERSION"
        }
    }

    object Android {
        object Tools {
            object Build {
                const val gradlePlugin = "com.android.tools.build:gradle:7.1.0-alpha08"
            }
            const val desugarJdk = "com.android.tools:desugar_jdk_libs:1.1.5"
        }
    }

    object AndroidX {
        object AppCompat {
            const val appCompat = "androidx.appcompat:appcompat:1.3.0-rc01"
            const val browser = "androidx.browser:browser:1.3.0"
        }

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha06"
        }
    }

    object ArkIvanov {
        object MviKotlin {
            private const val VERSION = "2.0.1"
            const val mvikotlin = "com.arkivanov.mvikotlin:mvikotlin:$VERSION"
            const val mvikotlinMain = "com.arkivanov.mvikotlin:mvikotlin-main:$VERSION"
            const val mvikotlinMainIosX64 = "com.arkivanov.mvikotlin:mvikotlin-main-iosx64:$VERSION"
            const val mvikotlinMainIosArm64 = "com.arkivanov.mvikotlin:mvikotlin-main-iosarm64:$VERSION"
            const val mvikotlinMainJs = "com.arkivanov.mvikotlin:mvikotlin-main-js:$VERSION"
            const val mvikotlinLogging = "com.arkivanov.mvikotlin:mvikotlin-logging:$VERSION"
            const val mvikotlinTimeTravel = "com.arkivanov.mvikotlin:mvikotlin-timetravel:$VERSION"
            const val mvikotlinExtensionsCoroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$VERSION"
            const val rx = "com.arkivanov.mvikotlin:rx:$VERSION"
        }

        object Decompose {
            private const val VERSION = "0.2.1"
            const val decompose = "com.arkivanov.decompose:decompose:$VERSION"
            const val decomposeIosX64 = "com.arkivanov.decompose:decompose-iosx64:$VERSION"
            const val decomposeIosArm64 = "com.arkivanov.decompose:decompose-iosarm64:$VERSION"
            const val decomposeJs = "com.arkivanov.decompose:decompose-js:$VERSION"
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
            const val sqljsDriver = "com.squareup.sqldelight:sqljs-driver:$VERSION"
        }
    }

    object CashApp {
        object Turbine {
            const val turbine = "app.cash.turbine:turbine:0.4.1"
        }
    }

    object TouchLab {
        object Kermit {
            const val kermit = "co.touchlab:kermit:0.1.8"
        }
    }

    object Slf4j {
        const val simple = "org.slf4j:slf4j-simple:1.7.30"
    }

    object OpenBakery {
        const val xcodePlugin = "gradle.plugin.org.openbakery:plugin:0.20.1"
    }
}
