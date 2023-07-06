package model

import formatDuration

data class Record(
    val id: String,
    val project: String,
    val task: String,
    val description: String,
    val duration: String
): TrackerListItem()

fun TrackerRecord.toPresentation(): Record =
    Record(
        id = id,
        project = project.key,
        task = task.name,
        description = description,
        duration = duration.formatDuration()
    )
