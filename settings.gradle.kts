pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
    
}
rootProject.name = "hn"


include(":android")
include(":desktop")
include(":common:compose-ui")
include(":common:root")
include(":common:main")
include(":common:detail")
