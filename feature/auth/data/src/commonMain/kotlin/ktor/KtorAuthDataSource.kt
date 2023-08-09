package ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class KtorAuthDataSource(private val httpClient: HttpClient) {

    suspend fun authenticate(userName: String, password: String): LoginResponseBody {
        val requestBody = LoginRequestBody(userName, password)
        return httpClient.post("/api/auth/login") {
            // TODO: move to const
            header("X-API-KEY", "l2ykp7jx1EbCg2gOVc8Wl2cVnkiU9gHxbN7o7f1-nyLfcprRZzz1jswZZVSHGR2ov36v0m8uUqF1sjDhnocT85BOi2gVV9mrIlWxwl5Ij2Ccs2ublBXrJnQfHOVhh58A")
            setBody(requestBody)
        }.body()
    }
}

@Serializable
data class LoginRequestBody(
    @SerialName("username")
    val userName: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class LoginResponseBody(
    @SerialName("token")
    val token: String,
    @SerialName("user")
    val user: UserRemote
)

@Serializable
data class UserRemote(
    @SerialName("id")
    val id: String
)
