package list.model

sealed interface TrackerRecordsAction {

    data class NavigateToRecordEditor(val recordId: String): TrackerRecordsAction

    object NavigateToCurrentRecord: TrackerRecordsAction

    object NavigateToNewRecord: TrackerRecordsAction
}
