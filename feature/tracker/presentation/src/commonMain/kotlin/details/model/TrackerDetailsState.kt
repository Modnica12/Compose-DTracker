package details.model

import kotlinx.datetime.LocalDate
import model.TrackerActivity
import model.TrackerProject
import model.TrackerTaskHint
import utils.formatDetails
import utils.getCurrentDateTime

data class TrackerDetailsState(
    val selectedProject: TrackerProject? = null,
    val selectedActivity: TrackerActivity? = null,
    val task: String = "",
    val description: String = "",
    val date: LocalDate = getCurrentDateTime().date,
    val startTime: String = getCurrentDateTime().time.formatDetails(),
    val endTime: String? = null,
    val projectSuggestions: List<TrackerProject> = emptyList(),
    val taskSuggestions: List<TrackerTaskHint> = emptyList(),
    val descriptionSuggestions: List<String> = emptyList(),
    val activitiesList: List<TrackerActivity> = emptyList(),
    val errorMessage: String? = null
)
