package ktor

import model.TrackerRecordRemote
import model.TrackerRecordsRequestBody
import model.TrackerRecordsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class KtorTrackerRecordsDataSource(private val httpClient: HttpClient) {

    suspend fun fetchRecords(): List<TrackerRecordRemote> {
        val id = "..." // paste your id
        val response: TrackerRecordsResponse = withContext(Dispatchers.IO) {
            httpClient.post("dtracker/list") {
                url {
                    appendPathSegments(id)
                }
                setBody(TrackerRecordsRequestBody())
            }.body()
        }
        return response.items
    }
}
