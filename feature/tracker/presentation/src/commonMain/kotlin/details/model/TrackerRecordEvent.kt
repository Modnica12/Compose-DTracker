package details.model

import details.autocomplete.TrackerDetailsTextField
import details.autocomplete.TrackerDetailsTextFieldSuggestion

internal sealed interface TrackerRecordEvent {

    data class TextFieldValueChanged(val textField: TrackerDetailsTextField) : TrackerRecordEvent

    data class TextFieldSuggestionClicked(
        val suggestion: TrackerDetailsTextFieldSuggestion
    ) : TrackerRecordEvent

    object SelectActivityClicked : TrackerRecordEvent

    data class ActivitySelected(val id: Int) : TrackerRecordEvent

    data class StartTimeChanged(val value: String) : TrackerRecordEvent

    data class EndTimeChanged(val value: String) : TrackerRecordEvent

    object CloseClicked : TrackerRecordEvent

    object CreateClicked : TrackerRecordEvent
}
