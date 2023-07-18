package model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerProject

@Serializable
internal data class TrackerProjectRemote(
    @SerialName("id")
    val id: Int,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("color")
    val color: String
)

internal fun TrackerProjectRemote.toDomain(): TrackerProject =
    TrackerProject(id = id, key = key, name = name, color = color)
