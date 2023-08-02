plugins {
    `android-setup`
    `multiplatform-setup`
    kotlin("plugin.serialization")
}

android {
    namespace = "${AppInfo.id}.auth.data"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:auth:api"))
                implementation(project(":common:core"))
                implementation(libs.kotlin.serialization.json)
                implementation(libs.settings)
            }
        }
    }
}
