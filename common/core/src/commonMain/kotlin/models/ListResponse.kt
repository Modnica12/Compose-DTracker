package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("items")
    val items: List<T>
)
