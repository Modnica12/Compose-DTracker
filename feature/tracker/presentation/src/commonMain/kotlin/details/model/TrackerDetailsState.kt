package details.model

import model.TrackerActivity
import model.TrackerProject
import model.TrackerTaskHint

data class TrackerDetailsState(
    val selectedProject: TrackerProject? = null,
    val selectedActivity: TrackerActivity? = null,
    val task: String = "",
    val description: String = "",
    val start: String = "",
    val projectSuggestions: List<TrackerProject> = emptyList(),
    val taskSuggestions: List<TrackerTaskHint> = emptyList(),
    val descriptionSuggestions: List<String> = emptyList(),
    val activitiesList: List<TrackerActivity> = emptyList(),
    val errorMessage: String? = null
)
