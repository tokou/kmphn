import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("multiplatform-setup")
    id("multiplatform-android-setup")
    id("kotlin-parcelize")
}

kotlin {

    val projectDependencies = listOf(
        project(":common:database"),
        project(":common:api"),
        project(":common:main"),
        project(":common:detail"),
        project(":common:utils")
    )

    ios {
        binaries {
            framework {
                baseName = "Hackernews"
                linkerOpts.add("-lsqlite3")
                projectDependencies.forEach { export(it) }

                when (val target = compilation.target.name) {
                    "iosX64" -> {
                        export(Deps.ArkIvanov.Decompose.decomposeIosX64)
                        export(Deps.ArkIvanov.MviKotlin.mvikotlinMainIosX64)
                    }
                    "iosArm64" -> {
                        export(Deps.ArkIvanov.Decompose.decomposeIosArm64)
                        export(Deps.ArkIvanov.MviKotlin.mvikotlinMainIosArm64)
                    }
                    else -> error("Unsupported target: $target")
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.ArkIvanov.Decompose.decompose)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlin)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlinExtensionsCoroutines)
                implementation(Deps.JetBrains.KotlinX.Coroutines.core)
                projectDependencies.forEach { implementation(it) }
            }
        }

        iosMain {
            dependencies {
                projectDependencies.forEach { api(it) }
            }
        }

        iosX64Main {
            dependencies {
                api(Deps.ArkIvanov.Decompose.decomposeIosX64)
                api(Deps.ArkIvanov.MviKotlin.mvikotlinMainIosX64)
            }
        }

        iosArm64Main {
            dependencies {
                api(Deps.ArkIvanov.Decompose.decomposeIosArm64)
                api(Deps.ArkIvanov.MviKotlin.mvikotlinMainIosArm64)
            }
        }
    }
}

fun getIosTarget(): String {
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    return if (sdkName.startsWith("iphoneos")) "iosArm64" else "iosX64"
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val targetName = getIosTarget()
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from(framework.outputDirectory)
    into(targetDir)
}
