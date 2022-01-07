@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
}

repositories {
    google()
}

android {

    // Workaround for https://youtrack.jetbrains.com/issue/KT-43944
    configurations {
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }

    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }

    sourceSets.getByName("main") {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs.plus(listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xallow-result-return-type"))
        }
    }
}
