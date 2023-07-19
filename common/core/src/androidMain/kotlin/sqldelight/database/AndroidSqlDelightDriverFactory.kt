package sqldelight.database

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import configuration.PlatformConfiguration

internal actual class SqlDelightDriverFactory actual constructor(
    private val platformConfiguration: PlatformConfiguration
) {

    actual fun createDriver(schema: SqlDriver.Schema, name: String): SqlDriver {
        return AndroidSqliteDriver(CoreDB.Schema, platformConfiguration.context, "core.db")
    }
}
