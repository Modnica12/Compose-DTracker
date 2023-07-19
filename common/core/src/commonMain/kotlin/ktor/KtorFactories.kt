package ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.withCharset
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.json.Json

fun createJson(): Json {
    return Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }
}

fun createHttpClient(json: Json): HttpClient {
    return HttpClient(KtorEngineFactory().createEngine()) {

        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }

        install(DefaultRequest)

        install(ContentNegotiation) {
            json(json)
        }

        defaultRequest {
            url(BASE_URL)
            header("Authorization", "Bearer $TOKEN")
            contentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
        }
    }
}

private const val BASE_URL = "https://api.is.doubletapp.io/api/"
private val TOKEN = "..." // paste your token
