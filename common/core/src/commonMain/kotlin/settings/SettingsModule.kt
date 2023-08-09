package settings

import org.koin.dsl.module

fun settingsModule() = module {
    single<EncryptedSettingsHolder> { EncryptedSettingsHolder(platformConfiguration = get()) }
}
