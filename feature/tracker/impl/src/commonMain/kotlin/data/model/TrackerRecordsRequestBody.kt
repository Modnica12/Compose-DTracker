package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackerRecordsRequestBody(
    @SerialName("project_ids")
    val projectIds: List<Int> = emptyList(),
    @SerialName("tasks")
    val tasks: List<String> = emptyList(),
    @SerialName("activity_ids")
    val activityIds: List<Int> = emptyList(),
    @SerialName("description_pattern")
    val descriptionPattern: String = ""
)
