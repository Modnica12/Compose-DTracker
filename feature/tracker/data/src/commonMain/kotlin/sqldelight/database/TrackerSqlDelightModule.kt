package sqldelight.database

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.cache.TrackerActivityCache
import model.cache.TrackerProjectCache
import model.cache.TrackerTaskCache
import org.koin.dsl.module

internal fun trackerSqlDelightModule() = module {
    single<ColumnAdapter<TrackerProjectCache, String>> {
        createJsonColumAdapter<TrackerProjectCache>()
    }
    single<ColumnAdapter<TrackerTaskCache, String>> {
        createJsonColumAdapter<TrackerTaskCache>()
    }
    single<ColumnAdapter<TrackerActivityCache, String>> {
        createJsonColumAdapter<TrackerActivityCache>()
    }
}

inline fun <reified T : Any> createJsonColumAdapter(): ColumnAdapter<T, String> {
    return object : ColumnAdapter<T, String> {
        override fun decode(databaseValue: String): T {
            return Json.decodeFromString(string = databaseValue)
        }

        override fun encode(value: T): String {
            return Json.encodeToString(value)
        }
    }
}
