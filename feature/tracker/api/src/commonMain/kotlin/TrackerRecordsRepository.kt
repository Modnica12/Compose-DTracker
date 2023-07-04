import model.TrackerRecord

interface TrackerRecordsRepository {

    suspend fun getRecords(): List<TrackerRecord>
}
