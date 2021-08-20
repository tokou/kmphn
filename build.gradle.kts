plugins {
    `kotlin-dsl`
    id("project-dependency-graph")
}

// Workaround for https://github.com/openbakery/gradle-xcodePlugin/issues/444
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("gradle.plugin.org.openbakery:plugin:0.20.1")
    }
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.squareup.okhttp" && requested.name == "okhttp") {
                useTarget("com.squareup.okhttp3:okhttp:4.9.1")
            }
        }
    }
}

group = "com.github.tokou"
version = "1.0.0"

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://kotlin.bintray.com/kotlinx")
    }
}
