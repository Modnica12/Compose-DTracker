package model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerTask

@Serializable
internal data class TrackerTaskRemote(
    @SerialName("name")
    val name: String,
    @SerialName("on_youtrack")
    val onYoutrack: Boolean
)

internal fun TrackerTaskRemote.toDomain(): TrackerTask =
    TrackerTask(name = name, onYoutrack = onYoutrack)
