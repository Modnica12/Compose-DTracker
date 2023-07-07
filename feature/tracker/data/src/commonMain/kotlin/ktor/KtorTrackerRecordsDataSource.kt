package ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import model.TrackerRecordRemote
import model.TrackerRecordsRequestBody
import model.TrackerRecordsResponse

internal class KtorTrackerRecordsDataSource(private val httpClient: HttpClient) {

    // try catch
    suspend fun fetchRecords(): List<TrackerRecordRemote> {
        val id = "..." // paste your id
        val response: TrackerRecordsResponse = httpClient.post("dtracker/list") {
            url {
                appendPathSegments(id)
            }
            setBody(TrackerRecordsRequestBody())
        }.body()
        return response.items
    }

    suspend fun fetchCurrent(): TrackerRecordRemote = httpClient.get("dtracker/current").body()

    suspend fun stopTracker(): TrackerRecordRemote = httpClient.put("dtracker/stop").body()
}
