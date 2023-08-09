plugins {
    `android-setup`
    `multiplatform-setup`
    `compose-setup`
}

android {
    namespace = "${AppInfo.id}.splash.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:core-ui"))
                implementation(project(":common:core-utils"))

                implementation(project(":feature:auth:api"))

                implementation(libs.kviewmodel.core)
                implementation(libs.kviewmodel.compose)
                implementation(libs.kviewmodel.odessey)

                implementation(libs.odessey.core)
                implementation(libs.odessey.compose)
            }
        }
    }
}
