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
                implementation(project(":common:core"))
                implementation(project(":common:core-utils"))
            }
        }
    }
}
