package details.model

import datetime.getCurrentDateTime
import kotlinx.datetime.LocalDate
import model.TrackerActivity
import model.TrackerProject
import utils.formatDetails

data class TrackerRecordViewState(
    val selectedProject: TrackerProject? = null,
    val selectedActivity: TrackerActivity? = null,
    val selectedTask: String = "",
    val selectedDescription: String = "",
    val date: LocalDate = getCurrentDateTime().date,
    val startTime: String = getCurrentDateTime().time.formatDetails(),
    val endTime: String? = null,
    val activitiesList: List<TrackerActivity> = emptyList(),
    val errorMessage: String? = null
)
