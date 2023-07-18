package model

import utils.formatDuration
import utils.getDate

data class TrackerDateGroup(
    val date: String,
    val totalTime: String,
    val items: List<TrackerListItem>
)

fun List<TrackerRecord>.toDateGroups(): List<TrackerDateGroup> {
    val dateGroupedItems = this
        .groupBy { record -> record.start.getDate() }
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

