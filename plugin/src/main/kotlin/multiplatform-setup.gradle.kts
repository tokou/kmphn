import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
}

kotlin {
    jvm("desktop")
    android()
    iosX64("ios")
    iosArm64("iosArm64")
    iosArm32("iosArm32")

    sourceSets {
        val iosMain by getting
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm32Main by getting {
            dependsOn(iosMain)
        }

        val commonTest by getting {
            dependencies {
                implementation(Deps.JetBrains.Kotlin.testCommon)
                implementation(Deps.JetBrains.Kotlin.testAnnotationsCommon)
                implementation(Deps.JetBrains.KotlinX.Coroutines.test)
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
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs.plus(listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xallow-result-return-type"))
            }
        }
    }
}
