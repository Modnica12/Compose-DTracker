plugins {
    `android-setup`
    `multiplatform-setup`
}

android {
    namespace = "${AppInfo.id}.core_utils"
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlin.datetime)
            }
        }
    }
}
