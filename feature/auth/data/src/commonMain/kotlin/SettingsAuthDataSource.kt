import com.russhwolf.settings.Settings

class SettingsAuthDataSource(private val settings: Settings) {

    fun saveToken(token: String) {
        settings.putString(AUTH_TOKEN_KEY, token)
    }

    fun getToken(): String? {
        return settings.getStringOrNull(AUTH_TOKEN_KEY)
    }

    fun saveUserId(userId: String) {
        settings.putString(USER_ID_KEY, userId)
    }

    fun getUserId(): String? {
        return settings.getStringOrNull(USER_ID_KEY)
    }

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token_key"
        private const val USER_ID_KEY = "user_id_key"
    }
}
