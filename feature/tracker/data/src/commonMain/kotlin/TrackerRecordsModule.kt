import ktor.KtorTrackerDataSource
import org.koin.dsl.module
import sqldelight.database.SqlDelightTrackerDataSource

fun recordsHistoryModule() = module {
    factory<KtorTrackerDataSource> { KtorTrackerDataSource(get()) }
    factory<SqlDelightTrackerDataSource> { SqlDelightTrackerDataSource(get()) }
    single<TrackerRecordsRepository> { TrackerRecordsRepositoryImpl(get()) }
}
