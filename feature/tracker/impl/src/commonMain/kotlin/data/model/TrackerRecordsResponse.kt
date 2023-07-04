package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TrackerRecordsResponse(
    @SerialName("items")
    val items: List<TrackerRecordRemote>
)
