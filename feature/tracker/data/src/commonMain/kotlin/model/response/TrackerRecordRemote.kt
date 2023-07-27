package model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerRecord
import utils.parseToDateTime

@Serializable
internal data class TrackerRecordRemote(
    @SerialName("id")
    val id: String,
    @SerialName("project")
    val project: TrackerProjectRemote,
    @SerialName("activity")
    val activity: TrackerActivityRemote?,
    @SerialName("task")
    val task: TrackerTaskRemote,
    @SerialName("start")
    val start: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("description")
    val description: String,
)

internal fun TrackerRecordRemote.toDomain(): TrackerRecord =
    TrackerRecord(
        id = id,
        project = project.toDomain(),
        activity = activity?.toDomain(),
        task = task.toDomain(),
        start = start.parseToDateTime(),
        duration = duration,
        description = description
    )
