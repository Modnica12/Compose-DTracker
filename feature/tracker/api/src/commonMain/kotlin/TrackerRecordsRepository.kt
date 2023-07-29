import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import model.TrackerActivity
import model.TrackerProject
import model.TrackerRecord
import model.TrackerTaskHint

interface TrackerRecordsRepository {

    val currentRecord: MutableStateFlow<TrackerRecord?>

    suspend fun getRecords(): Flow<List<TrackerRecord>>

    suspend fun getRecordWithId(id: String): TrackerRecord?

    suspend fun fetchRecords()

    suspend fun getCurrentRecord(): Result<Unit>

    suspend fun startTracker(
        projectId: Int,
        activityId: Int?,
        task: String,
        description: String,
        start: String
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
