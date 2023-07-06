plugins {
    `multiplatform-setup`
    `android-setup`
    `compose-setup`
}

android {
    namespace = "${AppInfo.id}.core_ui"
}
