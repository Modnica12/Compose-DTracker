package model.cache

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerProject

@Serializable
data class TrackerProjectCache(
    @SerialName("id")
    val id: Int,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("color")
    val color: String
)

internal fun TrackerProjectCache.toDomain(): TrackerProject =
    TrackerProject(id = id, key = key, name = name, color = color)

internal fun TrackerProject.toCache(): TrackerProjectCache =
    TrackerProjectCache(id = id, key = key, name = name, color = color)
