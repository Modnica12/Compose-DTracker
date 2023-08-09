import com.russhwolf.settings.Settings
import ktor.KtorAuthDataSource
import org.koin.dsl.module
import settings.EncryptedSettingsHolder

fun authModule() = module {
    single<Settings> { EncryptedSettingsHolder(platformConfiguration = get()).encryptedSettings }
    factory<KtorAuthDataSource> { KtorAuthDataSource(httpClient = get()) }
    factory<SettingsAuthDataSource> { SettingsAuthDataSource(settings = get()) }
    single<AuthRepository> {
        AuthRepositoryImpl(settingsDataSource = get(), remoteDataSource = get())
    }
}
