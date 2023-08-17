package ktor

import AuthTokenProvider
import org.koin.dsl.module

internal fun ktorModule() = module {
    single {
        createJson()
    }
    single {
        val tokenProvider: AuthTokenProvider = get()
        createHttpClient(json = get(), tokenProvider = tokenProvider)
    }
}
