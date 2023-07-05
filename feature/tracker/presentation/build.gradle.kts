plugins {
    `android-setup`
    `multiplatform-setup`
    `compose-setup`
}

android {
    namespace = "${AppInfo.id}.tracker.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:tracker:api"))
                implementation(project(":common:core"))
                implementation(libs.kviewmodel.core)
                implementation(libs.kviewmodel.compose)
            }
        }
    }
}
