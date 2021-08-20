@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
}

kotlin {
    jvm("desktop")
    android()
    ios()
    js("web", IR) {
        browser()
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(Deps.JetBrains.Kotlin.testCommon)
                implementation(Deps.JetBrains.Kotlin.testAnnotationsCommon)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(Deps.JetBrains.Kotlin.testJunit)
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(Deps.JetBrains.Kotlin.testJunit)
            }
        }

        val webTest by getting {
            dependencies {
                implementation(Deps.JetBrains.Kotlin.testJs)
            }
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            useIR = true
            jvmTarget = "11"
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs.plus(listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xallow-result-return-type"))
            }
        }
    }
}
