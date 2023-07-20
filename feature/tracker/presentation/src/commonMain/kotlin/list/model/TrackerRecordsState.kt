package list.model

import model.TrackerDateGroup
import model.details.TrackerRecordDetails

internal data class TrackerRecordsState(
    val screenState: TrackerRecordsScreenState = TrackerRecordsScreenState.Loading,
    val dateGroups: List<TrackerDateGroup> = emptyList(),
    val currentRecord: TrackerRecordDetails = TrackerRecordDetails.default
)
