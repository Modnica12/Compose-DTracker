interface AuthRepository {

    suspend fun authenticate(userName: String, password: String): Result<Unit>

    fun getAuthToken(): String?

    fun getUserId(): String?

    fun isAuthenticated(): Boolean
}
