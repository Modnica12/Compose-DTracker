import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ktor.KtorAuthDataSource

class AuthRepositoryImpl(
    private val remoteDataSource: KtorAuthDataSource,
    private val settingsDataSource: SettingsAuthDataSource
) : AuthRepository {

    override suspend fun authenticate(userName: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = remoteDataSource.authenticate(userName = userName, password = password)
                settingsDataSource.saveToken(token = response.token)
                settingsDataSource.saveUserId(userId = response.user.id)
                Result.success(Unit)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
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
