package list.model

import model.TrackerDateGroup
import model.details.TrackerRecordDetails

data class TrackerRecordsState(
    val dateGroups: List<TrackerDateGroup> = emptyList(),
    val currentRecord: TrackerRecordDetails = TrackerRecordDetails.default
)
