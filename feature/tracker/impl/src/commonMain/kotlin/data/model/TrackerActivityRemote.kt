package data.model

import model.TrackerActivity

internal data class TrackerActivityRemote(
    val id: Int,
    val name: String
)

internal fun TrackerActivityRemote.toDomain(): TrackerActivity =
    TrackerActivity(id = id, name = name)
