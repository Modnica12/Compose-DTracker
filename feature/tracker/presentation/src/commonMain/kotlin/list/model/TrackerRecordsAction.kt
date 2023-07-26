package list.model

sealed interface TrackerRecordsAction {

    data class NavigateToDetails(val recordId: String? = null): TrackerRecordsAction
}
