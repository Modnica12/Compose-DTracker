plugins {
    `android-setup`
    `multiplatform-setup`
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}

android {
    namespace = "${AppInfo.id}.tracker.data"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:tracker:api"))
                implementation(project(":common:core"))
                implementation(libs.kotlin.serialization.json)
            }
        }
    }
}

sqldelight {
    database("TrackerRecords") {
        packageName = "tracker.data.db"
        schemaOutputDirectory = file("src/commonMain/sqldelight/database")
    }
}
