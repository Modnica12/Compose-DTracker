package ktor

import com.russhwolf.settings.Settings
import org.koin.dsl.module

internal fun ktorModule() = module {
    single {
        createJson()
    }
    single {
        // TODO: использовать TokenProvider, реализуемый в модуле аутентификации
        //  т.к. AuthRepository зависит от core модуля мы не можем юзать его здесь
        val settings: Settings = get()
        createHttpClient(
            json = get(),
            tokenProvider = { settings.getStringOrNull("auth_token_key") }
        )
    }
}
