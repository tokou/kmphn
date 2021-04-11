plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

dependencies {

    implementation(Deps.JetBrains.Kotlin.stdlibJs)

    implementation(Deps.JetBrains.KotlinX.Html.htmlJs)

    implementation(Deps.JetBrains.Kotlin.React.react)
    implementation(Deps.JetBrains.Kotlin.React.reactDom)
    implementation(Deps.JetBrains.Kotlin.React.Router.reactRouterDom)

    implementation(npm(Deps.Npm.React.react, Deps.Npm.React.VERSION))
    implementation(npm(Deps.Npm.React.reactDom, Deps.Npm.React.VERSION))

    implementation(Deps.ArkIvanov.Decompose.decompose)
    implementation(Deps.ArkIvanov.Decompose.decomposeJs)

    implementation(Deps.ArkIvanov.MviKotlin.mvikotlin)
    implementation(Deps.ArkIvanov.MviKotlin.mvikotlinMain)
    implementation(Deps.ArkIvanov.MviKotlin.mvikotlinMainJs)

    implementation(project(":common:api"))
    implementation(project(":common:database"))
    implementation(project(":common:root"))
    implementation(project(":common:main"))
    implementation(project(":common:detail"))
    implementation(project(":common:utils"))
}

kotlin {
    js(LEGACY) {
        browser()
        binaries.executable()
    }
}

