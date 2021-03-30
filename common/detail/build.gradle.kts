plugins {
    id("multiplatform-setup")
    id("multiplatform-android-setup")
}

kotlin {
    sourceSets {
        val commonMain by getting {
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
    }
}
