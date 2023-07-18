package model.details

import model.TrackerProject

data class TrackerProject(
    val id: Int,
    val key: String
)

fun TrackerProject.toPresentation(): model.details.TrackerProject =
    TrackerProject(id = id, key = key)
