package list.model

import model.TrackerListItem


sealed interface TrackerRecordsEvent {

    object TrackerButtonClicked : TrackerRecordsEvent

    object StartClicked : TrackerRecordsEvent

    data class TaskGroupClicked(val taskGroup: TrackerListItem.TaskGroup) : TrackerRecordsEvent
}
