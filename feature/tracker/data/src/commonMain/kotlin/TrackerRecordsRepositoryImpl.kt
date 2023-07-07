import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ktor.KtorTrackerRecordsDataSource
import model.TrackerRecordRemote
import model.toDomain
import model.TrackerRecord

internal class TrackerRecordsRepositoryImpl(
    private val remoteSource: KtorTrackerRecordsDataSource
) : TrackerRecordsRepository {

    override suspend fun getRecords(): List<TrackerRecord> =
        withContext(Dispatchers.IO) {
            remoteSource.fetchRecords().map(TrackerRecordRemote::toDomain)
        }

    override suspend fun getCurrentRecord(): Result<TrackerRecord> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(remoteSource.fetchCurrent().toDomain())
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

    override suspend fun stopTracker(): Result<TrackerRecord> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(remoteSource.stopTracker().toDomain())
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }
}
