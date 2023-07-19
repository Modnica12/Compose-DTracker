package sqldelight.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import configuration.PlatformConfiguration

internal actual class SqlDelightDriverFactory actual constructor(
    private val platformConfiguration: PlatformConfiguration
){
    actual fun createDriver(schema: SqlDriver.Schema, name: String): SqlDriver {
        return NativeSqliteDriver(schema, name)
    }
}
