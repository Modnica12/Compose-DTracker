package ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.withCharset
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal fun ktorModule() = module {
    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }
    single {
        HttpClient(KtorEngineFactory().createEngine()) {

            install(Auth) {
                bearer {
                    loadTokens {
                        // refresh token нужен)
                        BearerTokens(TOKEN, "1")
                    }
                }
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }

            install(DefaultRequest)

            install(ContentNegotiation) {
                json(get())
            }

            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
            }
        }
    }
}

private const val BASE_URL = "https://api.is.doubletapp.io/api/"
private val TOKEN = "..." // paste your token
