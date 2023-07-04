package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.TrackerActivity

@Serializable
internal data class TrackerActivityRemote(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)

internal fun TrackerActivityRemote.toDomain(): TrackerActivity =
    TrackerActivity(id = id, name = name)
