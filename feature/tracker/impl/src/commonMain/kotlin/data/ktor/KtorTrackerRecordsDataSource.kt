package data.ktor

import data.model.TrackerRecordRemote
import io.ktor.client.HttpClient

internal class KtorTrackerRecordsDataSource(private val httpClient: HttpClient) {

    suspend fun fetchRecords(): List<TrackerRecordRemote> {
        return emptyList()
    }
}
