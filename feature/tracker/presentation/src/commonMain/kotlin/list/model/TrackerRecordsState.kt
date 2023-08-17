package list.model

import model.TrackerDateGroup
import model.details.TrackerRecordDetails

internal data class TrackerRecordsState(
    val screenState: TrackerRecordsScreenState = TrackerRecordsScreenState.Loading,
    val dateGroups: List<TrackerDateGroup> = emptyList(),
    val currentRecord: TrackerRecordDetails = TrackerRecordDetails.default,
    val recordDialogState: RecordDialogState = RecordDialogState.Hidden
)

internal sealed interface RecordDialogState {

    object Hidden: RecordDialogState

    data class Shown(val recordId: String): RecordDialogState
}
