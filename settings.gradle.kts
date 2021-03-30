pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}

rootProject.name = "hn"

includeBuild("plugin")

include(":android")
include(":desktop")
include(":ios")

include(":common:compose-ui")

include(":common:detail")
include(":common:main")
include(":common:root")

include(":common:api")
include(":common:database")

include(":common:utils")
