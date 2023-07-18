package model.details

import model.TrackerActivity

data class ActivityPresentation(
    val id: Int,
    val name: String
)

fun TrackerActivity.toPresentation(): ActivityPresentation =
    ActivityPresentation(id = id, name = name)
