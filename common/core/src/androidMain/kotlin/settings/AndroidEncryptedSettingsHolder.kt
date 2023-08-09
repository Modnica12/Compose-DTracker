package settings

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import configuration.PlatformConfiguration

actual class EncryptedSettingsHolder actual constructor(platformConfiguration: PlatformConfiguration) {

    actual val encryptedSettings: Settings = SharedPreferencesSettings(
        delegate = EncryptedSharedPreferences.create(
            ENCRYPTED_DATABASE_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            platformConfiguration.context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ),
        commit = false
    )
}
