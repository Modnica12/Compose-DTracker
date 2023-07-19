package sqldelight

import org.koin.dsl.module
import sqldelight.database.CoreDB
import sqldelight.database.SqlDelightDriverFactory
import sqldelightdatabase.CoreDBQueries

internal fun sqlDelightModule() = module {
    single<SqlDelightDriverFactory> {
        SqlDelightDriverFactory(get())
    }
    single<CoreDBQueries> {
        val driverFactory: SqlDelightDriverFactory = get()
        CoreDB(driverFactory.createDriver(CoreDB.Schema, "core.db")).coreDBQueries
    }
}
