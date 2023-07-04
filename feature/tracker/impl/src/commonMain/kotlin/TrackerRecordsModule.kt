import data.TrackerRecordsRepositoryImpl
import data.ktor.KtorTrackerRecordsDataSource
import org.koin.dsl.module

fun recordsHistoryModule() = module {
    factory<KtorTrackerRecordsDataSource> { KtorTrackerRecordsDataSource(get()) }
    single<TrackerRecordsRepository> { TrackerRecordsRepositoryImpl(get()) }
}
