package details.autocomplete

import model.TrackerProject
import model.TrackerTaskHint

internal data class TrackerDetailsTextFieldsState(
    val projectText: String = "",
    val projectSuggestions: List<TrackerProject> = emptyList(),
    val taskText: String = "",
    val taskSuggestions: List<TrackerTaskHint> = emptyList(),
    val descriptionText: String = "",
    val descriptionSuggestions: List<String> = emptyList(),
)
