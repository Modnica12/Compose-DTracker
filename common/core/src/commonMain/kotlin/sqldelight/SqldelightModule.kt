package sqldelight

import org.koin.dsl.module
import sqldelight.database.SqlDelightDriverFactory

internal fun sqlDelightModule() = module {
    single<SqlDelightDriverFactory> {
        SqlDelightDriverFactory(platformConfiguration = get())
    }
}
