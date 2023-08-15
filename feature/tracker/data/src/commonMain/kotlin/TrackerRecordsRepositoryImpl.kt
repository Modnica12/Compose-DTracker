import database.TrackerRecordCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import ktor.KtorTrackerDataSource
import model.TrackerActivity
import model.TrackerProject
import model.TrackerRecord
import model.TrackerTaskHint
import model.request.TrackerRecordRequestBody
import model.request.toRequestBody
import model.response.toDomain
import sqldelight.database.SqlDelightTrackerDataSource
import sqldelight.database.toCache
import sqldelight.database.toDomain
import utils.withResult

internal class TrackerRecordsRepositoryImpl(
    private val remoteSource: KtorTrackerDataSource,
    private val cacheSource: SqlDelightTrackerDataSource
) : TrackerRecordsRepository {

    private val mutableCurrentRecord: MutableStateFlow<TrackerRecord?> = MutableStateFlow(null)
    override val currentRecord: StateFlow<TrackerRecord?> = mutableCurrentRecord.asStateFlow()

    override fun setCurrentRecord(record: TrackerRecord?) {
        mutableCurrentRecord.value = record
    }

    override fun updateCurrentRecord(transform: TrackerRecord.() -> TrackerRecord?) {
        mutableCurrentRecord.update { it?.transform() }
    }

    override suspend fun getRecords(): Flow<List<TrackerRecord>> =
        cacheSource.getTrackerRecords()
            .flowOn(Dispatchers.IO)
            .map { records -> records.map(TrackerRecordCache::toDomain) }

    override suspend fun getRecordWithId(id: String): TrackerRecord? {
        return cacheSource.getRecordWithId(id = id)?.toDomain()
    }

    override suspend fun fetchUserRecords(userId: String): Result<Unit> =
        withResult {
            remoteSource.fetchRecords(userId).let { records ->
                cacheSource.clear()
                // Using same time format for remote and cache
                records.forEach { record -> cacheSource.insertRecord(record.toCache()) }
            }
        }

    override suspend fun getCurrentRecord(): Result<Unit> =
        withResult {
            val currentRecordRemote = remoteSource.fetchCurrent()
            mutableCurrentRecord.value = currentRecordRemote.toDomain()
        }

    override suspend fun startTracker(
        projectId: Int,
        activityId: Int?,
        task: String,
        description: String,
        start: LocalDateTime
    ): Result<TrackerRecord> = withResult {
        val requestBody = TrackerRecordRequestBody(
            projectId = projectId,
            activityId = activityId,
            task = task,
            description = description,
            // Mapping to format with Z at the end
            start = start.toInstant(timeZone = TimeZone.UTC).toString(),
            duration = null
        )
        val remoteRecord = remoteSource.startTracker(requestBody = requestBody)
        return@withResult remoteRecord.toDomain()
    }

    override suspend fun stopTracker(): Result<TrackerRecord> =
        withResult {
            mutableCurrentRecord.value = null
            val remoteRecord = remoteSource.stopTracker()
            return@withResult remoteRecord.toDomain()
        }

    override suspend fun addRecord(trackerRecord: TrackerRecord): Result<TrackerRecord> =
        withResult {
            val addedRecord = remoteSource.addTrackerRecord(
                requestBody = trackerRecord.toRequestBody()
            )
            return@withResult addedRecord.toDomain()
        }

    override suspend fun updateRecord(
        trackerRecord: TrackerRecord
    ): Result<TrackerRecord> = doWithResult {
        remoteSource.updateTrackerRecord(
            id = trackerRecord.id,
            requestBody = trackerRecord.toRequestBody()
        ).toDomain()
    }

    override suspend fun getProjects(key: String): Result<List<TrackerProject>> =
        withResult {
            val remoteProjects = remoteSource.getProjects(key = key).items
            return@withResult remoteProjects.map { it.toDomain() }
        }

    override suspend fun getTasks(
        projectIds: Int,
        pattern: String,
        limit: Int
    ): Result<List<TrackerTaskHint>> = withResult {
        val remoteTasks = remoteSource.getTasks(
            projectIds = projectIds,
            pattern = pattern,
            limit = limit
        )
        return@withResult remoteTasks.map { task -> task.toDomain() }
    }

    override suspend fun getDescriptions(pattern: String, limit: Int): Result<List<String>> =
        withResult {
            return@withResult remoteSource.getDescriptions(pattern = pattern, limit = limit)
        }

    override suspend fun getActivities(): Result<List<TrackerActivity>> =
        withResult {
            val remoteActivities = remoteSource.getActivities().items
            return@withResult remoteActivities.map { it.toDomain() }
        }
}
