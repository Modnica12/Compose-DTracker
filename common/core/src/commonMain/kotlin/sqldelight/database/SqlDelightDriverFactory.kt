package sqldelight.database

import com.squareup.sqldelight.db.SqlDriver
import configuration.PlatformConfiguration

internal expect class SqlDelightDriverFactory(platformConfiguration: PlatformConfiguration) {

    fun createDriver(schema: SqlDriver.Schema, name: String): SqlDriver
}
