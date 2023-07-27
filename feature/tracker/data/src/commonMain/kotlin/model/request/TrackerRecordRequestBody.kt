package model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerRecord

@Serializable
data class TrackerRecordRequestBody(
    @SerialName("project_id")
    val projectId: Int,
    @SerialName("activity_id")
    val activityId: Int?,
    @SerialName("task")
    val task: String,
    @SerialName("description")
    val description: String,
    @SerialName("start")
    val start: String,
    @SerialName("duration")
    val duration: Int?
)

fun TrackerRecord.toRequestBody(): TrackerRecordRequestBody =
    TrackerRecordRequestBody(
        projectId = project?.id ?: 0, // ПЛОХО
        activityId = activity?.id,
        task = task?.name ?: "",
        description = description,
        start = start.toString(), // хм)
        duration = duration
    )
