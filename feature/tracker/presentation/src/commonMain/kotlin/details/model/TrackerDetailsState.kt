package details.model

import model.TrackerActivity
import model.TrackerProject

data class TrackerDetailsState(
    val project: String = "",
    val selectedProject: TrackerProject? = null,
    val selectedActivity: TrackerActivity? = null,
    val task: String = "",
    val description: String = "",
    val start: String = "",
    val duration: Int = 0,
    val projectsSuggestions: List<TrackerProject> = emptyList(),
    val activitiesList: List<TrackerActivity> = emptyList(),
    val errorMessage: String? = null
)
