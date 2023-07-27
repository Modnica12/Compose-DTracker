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

                api(libs.kotlin.datetime)

                api(libs.koin)

                api(libs.ktor.core)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.auth)

                api(libs.sqldelight.core)
                api(libs.sqldelight.coroutines)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.android)
                implementation(libs.sqldelight.android)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.ios)
                implementation(libs.sqldelight.ios)
            }
        }
    }
}
