plugins {
    `android-setup`
    `multiplatform-setup`
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
