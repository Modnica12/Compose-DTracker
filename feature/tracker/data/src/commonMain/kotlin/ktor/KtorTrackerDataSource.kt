package ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import model.request.TrackerRecordRequestBody
import model.request.TrackerRecordsRequestBody
import model.response.SuccessResponse
import model.response.TrackerActivityRemote
import model.response.TrackerProjectRemote
import model.response.TrackerRecordRemote
import model.response.TrackerTaskHintRemote
import models.ListResponse

internal class KtorTrackerDataSource(private val httpClient: HttpClient) {

    suspend fun fetchRecords(userId: String): List<TrackerRecordRemote> {
        val response: ListResponse<TrackerRecordRemote> = httpClient.post("dtracker/list") {
            url {
                appendPathSegments(userId)
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

    suspend fun deleteTrackerRecord(
        id: String
    ): SuccessResponse = httpClient.delete("dtracker") {
        url {
            appendPathSegments(id)
        }
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

    suspend fun getTasks(projectIds: Int, pattern: String, limit: Int = 10): List<TrackerTaskHintRemote> =
        httpClient.get("dtracker/search-task-hint") {
            url {
                parameter(key = "project_ids", value = projectIds)
                parameter(key = "pattern", value = pattern)
                parameter(key = "limit", value = limit)
            }
        }.body()

    suspend fun getDescriptions(pattern: String, limit: Int = 10): List<String> =
        httpClient.get("dtracker/search-records-hint") {
            url {
                parameter(key = "pattern", value = pattern)
                parameter(key = "limit", value = limit)
            }
        }.body()

    suspend fun getActivities(): ListResponse<TrackerActivityRemote> =
        httpClient.get("dtracker/activities").body()
}
