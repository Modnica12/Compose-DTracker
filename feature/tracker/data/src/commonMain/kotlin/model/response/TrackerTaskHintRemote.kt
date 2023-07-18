package model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerTaskHint

@Serializable
internal data class TrackerTaskHintRemote(
    @SerialName("name")
    val name: String,
    @SerialName("summary")
    val summary: String,
    @SerialName("projects")
    val projects: List<TrackerProjectRemote>
)

internal fun TrackerTaskHintRemote.toDomain(): TrackerTaskHint =
    TrackerTaskHint(
        name = name,
        summary = summary
    )
