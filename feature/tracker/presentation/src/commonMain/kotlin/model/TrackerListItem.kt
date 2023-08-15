package model

import datetime.getCurrentDateTime
import kotlinx.datetime.LocalDate
import utils.formatDuration

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
        val date: LocalDate,
        val records: List<Record>,
        val isExpanded: Boolean
    ): TrackerListItem()
}

fun TrackerRecord.toPresentation(): TrackerListItem.Record =
    TrackerListItem.Record(
        id = id,
        project = project?.key ?: "",
        task = task?.name ?: "",
        description = description,
        duration = duration.formatDuration()
    )

fun List<TrackerRecord>.mapToTaskGroup(): TrackerListItem.TaskGroup =
    TrackerListItem.TaskGroup(
        name = firstOrNull()?.task?.name ?: "",
        project = firstOrNull()?.project?.key ?: "",
        totalTime = sumOf { item -> item.duration }.formatDuration(),
        date = firstOrNull()?.start?.date ?: getCurrentDateTime().date, // хм)
        records = map { it.toPresentation() },
        isExpanded = false
    )
