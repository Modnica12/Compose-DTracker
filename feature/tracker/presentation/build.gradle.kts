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
                implementation(project(":common:core-utils"))
                implementation(project(":common:core-ui"))

                implementation(libs.kviewmodel.core)
                implementation(libs.kviewmodel.compose)
                implementation(libs.kviewmodel.odessey)

                implementation(libs.odessey.core)
                implementation(libs.odessey.compose)
            }
        }
    }
}
