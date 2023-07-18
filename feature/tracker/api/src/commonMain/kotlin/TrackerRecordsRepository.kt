import kotlinx.coroutines.flow.MutableStateFlow
import model.TrackerActivity
import model.TrackerProject
import model.TrackerRecord

interface TrackerRecordsRepository {

    val currentRecord: MutableStateFlow<TrackerRecord?>

    suspend fun getRecords(): List<TrackerRecord>

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

    // мб id можно брать из модельки
    suspend fun updateRecord(id: String, trackerRecord: TrackerRecord): Result<TrackerRecord>

    suspend fun getProjects(key: String): Result<List<TrackerProject>>

    suspend fun getActivities(): Result<List<TrackerActivity>>
}
