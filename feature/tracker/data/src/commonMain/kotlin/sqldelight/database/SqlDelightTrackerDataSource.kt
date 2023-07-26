package sqldelight.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import database.TrackerRecordCache
import database.TrackerRecordsQueries
import kotlinx.coroutines.flow.Flow

class SqlDelightTrackerDataSource(private val trackerRecordsQueries: TrackerRecordsQueries) {

    fun getTrackerRecords(): Flow<List<TrackerRecordCache>> {
        return trackerRecordsQueries.selectAll().asFlow().mapToList()
    }

    fun getRecordWithId(id: String): TrackerRecordCache? {
        return trackerRecordsQueries.getById(id = id).executeAsOneOrNull()
    }

    suspend fun insertRecord(record: TrackerRecordCache) {
        trackerRecordsQueries.insert(record)
    }

    suspend fun clear() {
        trackerRecordsQueries.clear()
    }
}
