
import database.TrackerRecordCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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

internal class TrackerRecordsRepositoryImpl(
    private val remoteSource: KtorTrackerDataSource,
    private val cacheSource: SqlDelightTrackerDataSource
) : TrackerRecordsRepository {

    private val mutableCurrentRecord: MutableStateFlow<TrackerRecord?> = MutableStateFlow(null)
    override val currentRecord: StateFlow<TrackerRecord?> = mutableCurrentRecord.asStateFlow()

    override fun updateCurrentRecord(transform: TrackerRecord.() -> TrackerRecord?) {
        mutableCurrentRecord.value = mutableCurrentRecord.value?.transform()
    }

    override suspend fun getRecords(): Flow<List<TrackerRecord>> =
        cacheSource.getTrackerRecords()
            .flowOn(Dispatchers.IO)
            .map { records -> records.map(TrackerRecordCache::toDomain) }

    override suspend fun getRecordWithId(id: String): TrackerRecord? {
        return cacheSource.getRecordWithId(id = id)?.toDomain()
    }

    override suspend fun fetchRecords() = withContext(Dispatchers.IO) {
        remoteSource.fetchRecords().let { records ->
            cacheSource.clear()
            // Using same time format for remote and cache
            records.forEach { record -> cacheSource.insertRecord(record.toCache()) }
        }
    }

    override suspend fun getCurrentRecord(): Result<Unit> =
        // TODO: вынести в отдельную функцию withContext и try catch
        withContext(Dispatchers.IO) {
            try {
                mutableCurrentRecord.value = remoteSource.fetchCurrent().toDomain()
                Result.success(Unit)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

    override suspend fun startTracker(
        projectId: Int,
        activityId: Int?,
        task: String,
        description: String,
        start: LocalDateTime
    ): Result<TrackerRecord> = withContext(Dispatchers.IO) {
        try {
            val requestBody = TrackerRecordRequestBody(
                projectId = projectId,
                activityId = activityId,
                task = task,
                description = description,
                // Mapping to format with Z at the end
                start = start.toInstant(timeZone = TimeZone.UTC).toString(),
                duration = null
            )
            val record = remoteSource.startTracker(requestBody = requestBody).toDomain()
            Result.success(record)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun stopTracker(): Result<TrackerRecord> =
        withContext(Dispatchers.IO) {
            try {
                mutableCurrentRecord.value = null
                Result.success(remoteSource.stopTracker().toDomain())
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

    override suspend fun addRecord(trackerRecord: TrackerRecord): Result<TrackerRecord> =
        withContext(Dispatchers.IO) {
            try {
                val addedRecord = remoteSource.addTrackerRecord(
                    requestBody = trackerRecord.toRequestBody()
                ).toDomain()
                Result.success(addedRecord)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

    override suspend fun updateRecord(
        id: String,
        trackerRecord: TrackerRecord
    ): Result<TrackerRecord> = withContext(Dispatchers.IO) {
        try {
            val updatedRecord = remoteSource.updateTrackerRecord(
                id = id,
                requestBody = trackerRecord.toRequestBody()
            ).toDomain()
            Result.success(updatedRecord)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun getProjects(key: String): Result<List<TrackerProject>> =
        withContext(Dispatchers.IO) {
            try {
                val projects = remoteSource.getProjects(key = key).items.map { it.toDomain() }
                Result.success(projects)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

    override suspend fun getTasks(
        projectIds: Int,
        pattern: String,
        limit: Int
    ): Result<List<TrackerTaskHint>> =
        withContext(Dispatchers.IO) {
            try {
                val tasks = remoteSource.getTasks(
                    projectIds = projectIds,
                    pattern = pattern,
                    limit = limit
                )
                    .map { task -> task.toDomain() }
                Result.success(tasks)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

    override suspend fun getDescriptions(pattern: String, limit: Int): Result<List<String>> =
        withContext(Dispatchers.IO) {
            try {
                val descriptions = remoteSource.getDescriptions(
                    pattern = pattern,
                    limit = limit
                )
                Result.success(descriptions)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }

    override suspend fun getActivities(): Result<List<TrackerActivity>> =
        withContext(Dispatchers.IO) {
            try {
                val activities = remoteSource.getActivities().items.map { it.toDomain() }
                Result.success(activities)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        }
}
