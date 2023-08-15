plugins {
    `multiplatform-setup`
    `android-setup`
    `compose-setup`
}

android {
    namespace = "${AppInfo.id}.core_ui"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.datetime)

                implementation(libs.kviewmodel.core)
                implementation(libs.kviewmodel.compose)

                implementation(compose.materialIconsExtended)
            }
        }
    }
}
