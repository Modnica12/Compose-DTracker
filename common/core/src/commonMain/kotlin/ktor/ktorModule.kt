package ktor

import org.koin.dsl.module

internal fun ktorModule() = module {
    single {
        createJson()
    }
    single {
        createHttpClient(json = get())
    }
}
