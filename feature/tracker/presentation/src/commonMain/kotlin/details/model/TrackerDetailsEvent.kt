package details.model

import details.autocomplete.TrackerDetailsTextField
import details.autocomplete.TrackerDetailsTextFieldSuggestion

internal sealed interface TrackerDetailsEvent {

    data class TextFieldValueChanged(val textField: TrackerDetailsTextField) : TrackerDetailsEvent

    data class TextFieldSuggestionClicked(
        val suggestion: TrackerDetailsTextFieldSuggestion
    ) : TrackerDetailsEvent

    object SelectActivityClicked : TrackerDetailsEvent

    data class ActivitySelected(val id: Int) : TrackerDetailsEvent

    data class StartTimeChanged(val value: String) : TrackerDetailsEvent

    data class EndTimeChanged(val value: String) : TrackerDetailsEvent

    object CloseClicked : TrackerDetailsEvent

    object CreateClicked : TrackerDetailsEvent
}
