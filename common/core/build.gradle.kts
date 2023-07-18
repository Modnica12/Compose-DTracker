plugins {
    `android-setup`
    `multiplatform-setup`
    kotlin("plugin.serialization")
}

android {
    namespace = "${AppInfo.id}.core"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlin.serialization)

                api(libs.koin)

                api(libs.ktor.core)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.auth)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.android)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.ios)
            }
        }
    }
}
