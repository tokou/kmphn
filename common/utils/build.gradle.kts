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
                implementation(Deps.ArkIvanov.MviKotlin.rx)
                implementation(Deps.JetBrains.KotlinX.Coroutines.core)
                api(Deps.JetBrains.KotlinX.DateTime.dateTime)
                api(Deps.TouchLab.Kermit.kermit)
            }
        }
    }
}
