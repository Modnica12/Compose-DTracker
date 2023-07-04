package data.model

import model.TrackerRecord

internal data class TrackerRecordRemote(
    val id: String,
    val project: TrackerProjectRemote,
    val activity: TrackerActivityRemote,
    val task: TrackerTaskRemote,
    val start: String, // Заюзать сразу какой-нибудь Date
    val duration: Int,
    val description: String,
)

internal fun TrackerRecordRemote.toDomain(): TrackerRecord =
    TrackerRecord(
        id = id,
        project = project.toDomain(),
        activity = activity.toDomain(),
        task = task.toDomain(),
        start = start,
        duration = duration,
        description = description
    )
