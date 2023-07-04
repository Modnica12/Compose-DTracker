plugins {
    `android-setup`
    `multiplatform-setup`
    kotlin("plugin.serialization")
}

android {
    namespace = "${AppInfo.id}.tracker.data"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:tracker:api"))
                implementation(project(":common:core"))
            }
        }
    }
}
