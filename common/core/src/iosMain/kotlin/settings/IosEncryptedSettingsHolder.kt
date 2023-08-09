package settings

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import configuration.PlatformConfiguration

actual class EncryptedSettingsHolder actual constructor(platformConfiguration: PlatformConfiguration) {

    @ExperimentalSettingsImplementation
    actual val encryptedSettings: Settings = KeychainSettings(service = ENCRYPTED_DATABASE_NAME)
}
