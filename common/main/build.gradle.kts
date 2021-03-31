plugins {
    id("multiplatform-setup")
    id("multiplatform-android-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.ArkIvanov.Decompose.decompose)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlin)
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlinExtensionsCoroutines)
                implementation(Deps.JetBrains.KotlinX.Coroutines.core)
                implementation(project(":common:api"))
                implementation(project(":common:database"))
                implementation(project(":common:utils"))
            }
        }
        commonTest {
            dependencies {
                implementation(Deps.ArkIvanov.MviKotlin.mvikotlinMain)
                implementation(Deps.CashApp.Turbine.turbine)
            }
        }
        desktopMain {
            dependencies {
                implementation(Deps.JetBrains.KotlinX.Coroutines.swing)
            }
        }
    }
}
