package model

import formatDuration

data class TaskGroup(
    val name: String,
    val project: String,
    val totalTime: String,
    val records: List<Record>
): TrackerListItem()

fun List<TrackerRecord>.mapToTaskGroup(): TaskGroup =
    TaskGroup(
        name = firstOrNull()?.task?.name ?: "",
        project = firstOrNull()?.project?.key ?: "",
        totalTime = sumOf { item -> item.duration }.formatDuration(),
        records = map { it.toPresentation() }
    )
