import ktor.KtorTrackerRecordsDataSource
import model.TrackerRecordRemote
import model.toDomain
import model.TrackerRecord

internal class TrackerRecordsRepositoryImpl(
    private val remoteSource: KtorTrackerRecordsDataSource
): TrackerRecordsRepository {

    override suspend fun getRecords(): List<TrackerRecord> {
        return remoteSource.fetchRecords().map(TrackerRecordRemote::toDomain)
    }
}
