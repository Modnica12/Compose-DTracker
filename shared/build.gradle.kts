plugins {
    `multiplatform-setup`
    `android-setup`
    `compose-setup`
    kotlin("native.cocoapods")
}

kotlin {
    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
            export(project(":common:core"))
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":common:core"))
                implementation(project(":common:core-ui"))
                implementation(project(":common:core-utils"))

                implementation(project(":feature:splash:presentation"))

                implementation(project(":feature:tracker:data"))
                implementation(project(":feature:tracker:presentation"))

                implementation(project(":feature:auth:data"))
                implementation(project(":feature:auth:presentation"))

                implementation(libs.odessey.core)
                implementation(libs.odessey.compose)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.kviewmodel.odessey)
                implementation(libs.accompanist.systemuicontroller)
            }
        }

        iosMain {
            dependencies {
                api(project(":common:core"))
            }
        }
    }
}

android {
    namespace = "${AppInfo.id}.common"
}
