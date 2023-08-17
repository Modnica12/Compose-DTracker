import settings.SettingsAuthDataSource

class AuthTokenProviderImpl(
    private val settingsDataSource: SettingsAuthDataSource
): AuthTokenProvider {

    override fun getAuthToken(): String? {
        return settingsDataSource.getToken()
    }
}
