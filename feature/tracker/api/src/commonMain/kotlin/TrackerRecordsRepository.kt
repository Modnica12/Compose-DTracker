import model.TrackerRecord

interface TrackerRecordsRepository {

    suspend fun getRecords(): List<TrackerRecord>

    suspend fun getCurrentRecord(): Result<TrackerRecord>

    suspend fun stopTracker(): Result<TrackerRecord>
}
