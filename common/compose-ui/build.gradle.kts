plugins {
    id("multiplatform-compose-setup")
    id("multiplatform-android-setup")
}

android {
    sourceSets {
        val main by getting {
            res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        }
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.ArkIvanov.Decompose.decompose)
                implementation(Deps.ArkIvanov.Decompose.extensionsCompose)
                implementation(Deps.JetBrains.KotlinX.Coroutines.core)
                implementation(project(":common:main"))
                implementation(project(":common:detail"))
                implementation(project(":common:root"))
                implementation(project(":common:utils"))
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.desktop.common)
            }
        }
    }
}
