package sqldelight.database

import com.squareup.sqldelight.db.SqlDriver
import configuration.PlatformConfiguration

expect class SqlDelightDriverFactory(platformConfiguration: PlatformConfiguration) {

    fun createDriver(schema: SqlDriver.Schema, name: String): SqlDriver
}
