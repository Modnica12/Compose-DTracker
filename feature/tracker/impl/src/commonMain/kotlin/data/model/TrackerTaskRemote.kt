package data.model

import model.TrackerTask

internal data class TrackerTaskRemote(
    val name: String,
    val onYoutrack: Boolean
)

internal fun TrackerTaskRemote.toDomain(): TrackerTask =
    TrackerTask(name = name, onYoutrack = onYoutrack)
