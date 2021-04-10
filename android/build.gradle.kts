
plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation(project(":common:compose-ui"))
    implementation(project(":common:database"))
    implementation(project(":common:api"))
    implementation(project(":common:root"))
    implementation(project(":common:utils"))
    implementation(Deps.ArkIvanov.Decompose.decompose)
    implementation(Deps.ArkIvanov.Decompose.extensionsCompose)
    implementation(Deps.ArkIvanov.MviKotlin.mvikotlin)
    implementation(Deps.ArkIvanov.MviKotlin.mvikotlinMain)
    implementation(Deps.ArkIvanov.MviKotlin.mvikotlinLogging)
    implementation(Deps.ArkIvanov.MviKotlin.mvikotlinExtensionsCoroutines)
    implementation(compose.material)
    implementation(Deps.AndroidX.AppCompat.browser)
    implementation(Deps.AndroidX.AppCompat.appCompat)
    implementation(Deps.AndroidX.Activity.activityCompose)
    coreLibraryDesugaring(Deps.Android.Tools.desugarJdk)
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.github.tokou.android"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0.0"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    kotlinOptions {
        useIR = true
    }
}

project.afterEvaluate {
    android.applicationVariants.forEach { variant ->
        tasks.create("installRun${variant.name.capitalize()}", type = Exec::class) {
            group = "run"
            dependsOn("install${variant.name.capitalize()}")
            commandLine = listOf("adb", "shell", "monkey", "-p", variant.applicationId + " 1")
            doLast {
                println("Launching ${variant.applicationId}")
            }
        }
    }
}
