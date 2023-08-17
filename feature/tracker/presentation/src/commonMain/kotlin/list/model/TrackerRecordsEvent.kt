package list.model

import model.TrackerListItem


sealed interface TrackerRecordsEvent {

    object TrackerButtonClicked : TrackerRecordsEvent

    object StartClicked : TrackerRecordsEvent

    object BottomBarClicked: TrackerRecordsEvent

    object DismissRecordDialog: TrackerRecordsEvent

    data class TaskGroupClicked(val taskGroup: TrackerListItem.TaskGroup) : TrackerRecordsEvent

    data class RecordClicked(val recordId: String) : TrackerRecordsEvent

    data class RecordLongClicked(val recordId: String) : TrackerRecordsEvent

    data class RunRecordClicked(val recordId: String) : TrackerRecordsEvent
}
