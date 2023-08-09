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

                api(libs.sqldelight.core)
                api(libs.sqldelight.coroutines)

                api(libs.settings)
                api("androidx.security:security-crypto:1.0.0")

                implementation(project(":feature:auth:api"))
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
