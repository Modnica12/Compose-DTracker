import database.TrackerRecordsQueries
import ktor.KtorTrackerDataSource
import model.cache.TrackerActivityCache
import model.cache.TrackerProjectCache
import model.cache.TrackerTaskCache
import org.koin.dsl.module
import sqldelight.database.SqlDelightDriverFactory
import sqldelight.database.SqlDelightTrackerDataSource
import sqldelight.database.createJsonColumAdapter
import sqldelight.database.trackerSqlDelightModule
import tracker.data.db.TrackerRecords

fun recordsHistoryModule() = module {
    includes(trackerSqlDelightModule())
    factory<KtorTrackerDataSource> { KtorTrackerDataSource(httpClient = get()) }
    single<TrackerRecordsQueries> {
        val driverFactory: SqlDelightDriverFactory = get()
        TrackerRecords(
            driver = driverFactory.createDriver(
                TrackerRecords.Schema,
                "trackerRecordsDB"
            ),
            // Koin don't work with adapters
            TrackerRecordCacheAdapter = database.TrackerRecordCache.Adapter(
                projectAdapter = createJsonColumAdapter<TrackerProjectCache>(),
                activityAdapter = createJsonColumAdapter<TrackerActivityCache>(),
                taskAdapter = createJsonColumAdapter<TrackerTaskCache>()
            ),
        ).trackerRecordsQueries
    }
    factory<SqlDelightTrackerDataSource> { SqlDelightTrackerDataSource(trackerRecordsQueries = get()) }
    single<TrackerRecordsRepository> { TrackerRecordsRepositoryImpl(remoteSource = get(), cacheSource = get()) }
}
