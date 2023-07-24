package model.cache

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerActivity

@Serializable
data class TrackerActivityCache(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)

internal fun TrackerActivityCache.toDomain(): TrackerActivity =
    TrackerActivity(id = id, name = name)

internal fun TrackerActivity.toCache(): TrackerActivityCache =
    TrackerActivityCache(id = id, name = name)
