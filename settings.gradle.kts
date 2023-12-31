pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "ComposeDTracker"

include(":androidApp")
include(":common:core")
include(":common:core-utils")
include(":common:core-ui")
include(":shared")

include(":feature:splash:presentation")

include(":feature:tracker:api")
include(":feature:tracker:data")
include(":feature:tracker:presentation")

include(":feature:auth:api")
include(":feature:auth:data")
include(":feature:auth:presentation")
