package model

import formatDuration

sealed class TrackerListItem {

    data class Record(
        val id: String,
        val project: String,
        val task: String,
        val description: String,
        val duration: String
    ) : TrackerListItem()

    data class TaskGroup(
        val name: String,
        val project: String,
        val totalTime: String,
        val records: List<Record>
    ): TrackerListItem()
}

fun TrackerRecord.toPresentation(): TrackerListItem.Record =
    TrackerListItem.Record(
        id = id,
        project = project.key,
        task = task.name,
        description = description,
        duration = duration.formatDuration()
    )

fun List<TrackerRecord>.mapToTaskGroup(): TrackerListItem.TaskGroup =
    TrackerListItem.TaskGroup(
        name = firstOrNull()?.task?.name ?: "",
        project = firstOrNull()?.project?.key ?: "",
        totalTime = sumOf { item -> item.duration }.formatDuration(),
        records = map { it.toPresentation() }
    )
