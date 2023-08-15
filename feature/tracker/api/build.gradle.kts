plugins {
    `android-setup`
    `multiplatform-setup`
}

android {
    namespace = "${AppInfo.id}.tracker.api"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":common:core-utils"))
                api(project(":feature:auth:api"))

                implementation(libs.kotlin.coroutines.core)
            }
        }
    }
}
