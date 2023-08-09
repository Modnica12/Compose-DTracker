package settings

import com.russhwolf.settings.Settings
import configuration.PlatformConfiguration

expect class EncryptedSettingsHolder(platformConfiguration: PlatformConfiguration) {
    val encryptedSettings: Settings
}

internal const val ENCRYPTED_DATABASE_NAME = "ENCRYPTED_SETTINGS"
