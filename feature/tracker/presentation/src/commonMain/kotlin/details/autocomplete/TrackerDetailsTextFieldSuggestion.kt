package details.autocomplete

import model.TrackerProject
import model.TrackerTaskHint

sealed interface TrackerDetailsTextFieldSuggestion {

    data class Project(val project: TrackerProject): TrackerDetailsTextFieldSuggestion

    data class Task(val task: TrackerTaskHint): TrackerDetailsTextFieldSuggestion

    data class Description(val text: String): TrackerDetailsTextFieldSuggestion
}
