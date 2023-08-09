
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime
import model.TrackerActivity
import model.TrackerProject
import model.TrackerRecord
import model.TrackerTaskHint

interface TrackerRecordsRepository {

    val currentRecord: StateFlow<TrackerRecord?>

    fun setCurrentRecord(record: TrackerRecord?)

    fun updateCurrentRecord(transform: TrackerRecord.() -> TrackerRecord?)

    suspend fun getRecords(): Flow<List<TrackerRecord>>

    suspend fun getRecordWithId(id: String): TrackerRecord?

    suspend fun fetchUserRecords(userId: String): Result<Unit>

    suspend fun getCurrentRecord(): Result<Unit>

    suspend fun startTracker(
        projectId: Int,
        activityId: Int?,
        task: String,
        description: String,
        start: LocalDateTime
    ): Result<TrackerRecord>

    suspend fun stopTracker(): Result<TrackerRecord>

    suspend fun addRecord(trackerRecord: TrackerRecord): Result<TrackerRecord>

    // TODO: мб id можно брать из модельки
    suspend fun updateRecord(id: String, trackerRecord: TrackerRecord): Result<TrackerRecord>

    suspend fun getProjects(key: String): Result<List<TrackerProject>>

    suspend fun getTasks(projectIds: Int, pattern: String, limit: Int = 10): Result<List<TrackerTaskHint>>

    suspend fun getDescriptions(pattern: String, limit: Int = 10): Result<List<String>>

    suspend fun getActivities(): Result<List<TrackerActivity>>
}
