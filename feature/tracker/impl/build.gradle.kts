plugins {
    `android-setup`
    `multiplatform-setup`
    kotlin("plugin.serialization")
}

android {
    namespace = "${AppInfo.id}.tracker"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:tracker:api"))
                implementation(project(":common:core"))
            }
        }
    }
}
