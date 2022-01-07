plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

repositories {
    maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {

    implementation(Deps.JetBrains.Kotlin.stdlibJs)
    implementation(Deps.JetBrains.KotlinX.Html.htmlJs)
    implementation(Deps.JetBrains.Kotlin.Wrappers.React.react)
    implementation(Deps.JetBrains.Kotlin.Wrappers.React.reactDom)
    implementation(Deps.JetBrains.Kotlin.Wrappers.Styled.styled)

    implementation(npm(Deps.Npm.React.react, Deps.Npm.React.VERSION))
    implementation(npm(Deps.Npm.React.reactDom, Deps.Npm.React.VERSION))

    implementation(Deps.ArkIvanov.MviKotlin.mvikotlin)

    implementation(project(":common:api"))
    implementation(project(":common:database"))
    implementation(project(":common:root"))
    implementation(project(":common:main"))
    implementation(project(":common:detail"))
    implementation(project(":common:utils"))
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
    }
}
