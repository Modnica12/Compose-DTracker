package data.model

import model.TrackerProject

internal data class TrackerProjectRemote(
    val id: Int,
    val key: String,
    val name: String,
    val color: String
)

internal fun TrackerProjectRemote.toDomain(): TrackerProject =
    TrackerProject(id = id, key = key, name = name, color = color)
