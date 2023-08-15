
import ktor.KtorAuthDataSource
import utils.withResult

class AuthRepositoryImpl(
    private val remoteDataSource: KtorAuthDataSource,
    private val settingsDataSource: SettingsAuthDataSource
) : AuthRepository {

    override suspend fun authenticate(userName: String, password: String): Result<Unit> =
        withResult {
            val response = remoteDataSource.authenticate(userName = userName, password = password)
            settingsDataSource.saveToken(token = response.token)
            settingsDataSource.saveUserId(userId = response.user.id)
        }

    override fun getAuthToken(): String? {
        return settingsDataSource.getToken()
    }

    override fun getUserId(): String? {
        return settingsDataSource.getUserId()
    }

    override fun isAuthenticated(): Boolean {
        println(settingsDataSource.getToken())
        return settingsDataSource.getToken() != null
    }
}
