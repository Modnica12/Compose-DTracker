package model

import kotlinx.datetime.LocalDate
import utils.formatDuration

data class TrackerDateGroup(
    val date: LocalDate,
    val totalTime: String,
    val items: List<TrackerListItem>
)

fun List<TrackerRecord>.toDateGroups(): List<TrackerDateGroup> {
    val dateGroupedItems = this
        .groupBy { record -> record.start.date }
        .map { (date, records) ->
            val taskGroupedItems = records.groupBy { it.task?.name }
            TrackerDateGroup(
                date = date,
                totalTime = records.sumOf { item -> item.duration }.formatDuration(),
                items = taskGroupedItems.map { (_, taskItems) ->
                    if (taskItems.size == 1) {
                        taskItems.first().toPresentation()
                    } else {
                        taskItems.mapToTaskGroup()
                    }
                }
            )
        }
    return dateGroupedItems
}

