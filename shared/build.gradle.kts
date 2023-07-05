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
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":feature:tracker:data"))
                implementation(project(":feature:tracker:presentation"))
            }
        }
    }
}

android {
    namespace = "${AppInfo.id}.common"
}
