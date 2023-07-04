package data

import TrackerRecordsRepository
import data.ktor.KtorTrackerRecordsDataSource
import data.model.TrackerRecordRemote
import data.model.toDomain
import model.TrackerRecord

internal class TrackerRecordsRepositoryImpl(
    private val remoteSource: KtorTrackerRecordsDataSource
): TrackerRecordsRepository {

    override suspend fun getRecords(): List<TrackerRecord> {
        return remoteSource.fetchRecords().map(TrackerRecordRemote::toDomain)
    }
}
