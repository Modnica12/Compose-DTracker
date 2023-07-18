package ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import model.TrackerActivityRemote
import model.TrackerProjectRemote
import model.TrackerRecordRemote
import model.TrackerRecordRequestBody
import model.TrackerRecordsRequestBody
import models.ListResponse

internal class KtorTrackerRecordsDataSource(private val httpClient: HttpClient) {

    // try catch
    suspend fun fetchRecords(): List<TrackerRecordRemote> {
        val id = "..." // paste your id
        val response: ListResponse<TrackerRecordRemote> = httpClient.post("dtracker/list") {
            url {
                appendPathSegments(id)
            }
            setBody(TrackerRecordsRequestBody())
        }.body()
        return response.items
    }

    suspend fun fetchCurrent(): TrackerRecordRemote = httpClient.get("dtracker/current").body()

    suspend fun startTracker(requestBody: TrackerRecordRequestBody): TrackerRecordRemote =
        httpClient.post("dtracker/start") {
            setBody(requestBody)
        }.body()

    suspend fun stopTracker(): TrackerRecordRemote = httpClient.put("dtracker/stop").body()

    suspend fun addTrackerRecord(
        requestBody: TrackerRecordRequestBody
    ): TrackerRecordRemote = httpClient.post("dtracker") {
        setBody(requestBody)
    }.body()

    suspend fun updateTrackerRecord(
        id: String,
        requestBody: TrackerRecordRequestBody
    ): TrackerRecordRemote = httpClient.put("dtracker") {
        url {
            appendPathSegments(id)
        }
        setBody(requestBody)
    }.body()

    suspend fun getProjects(key: String): ListResponse<TrackerProjectRemote> =
        httpClient.get("dtracker/projects") {
        url {
            parameter(key = "key", value = key)
        }
    }.body()

    suspend fun getActivities(): ListResponse<TrackerActivityRemote> =
        httpClient.get("dtracker/activities").body()
}
