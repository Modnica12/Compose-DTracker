package model.cache

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerTask
import model.response.TrackerTaskRemote

@Serializable
data class TrackerTaskCache(
    @SerialName("name")
    val name: String,
    @SerialName("on_youtrack")
    val onYoutrack: Boolean
)

internal fun TrackerTaskCache.toDomain(): TrackerTask =
    TrackerTask(name = name, onYoutrack = onYoutrack)

internal fun TrackerTask.toCache(): TrackerTaskCache =
    TrackerTaskCache(name = name, onYoutrack = onYoutrack)

internal fun TrackerTaskRemote.toCache(): TrackerTaskCache =
    TrackerTaskCache(name = name, onYoutrack = onYoutrack)
