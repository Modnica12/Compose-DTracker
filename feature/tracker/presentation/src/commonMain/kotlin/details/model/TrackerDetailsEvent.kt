package details.model

import model.TrackerTaskHint

sealed interface TrackerDetailsEvent {

    data class ProjectValueChanged(val value: String) : TrackerDetailsEvent

    data class ProjectSelected(val id: Int) : TrackerDetailsEvent

    data class TaskValueChanged(val value: String) : TrackerDetailsEvent

    data class TaskSelected(val taskHint: TrackerTaskHint) : TrackerDetailsEvent

    data class DescriptionValueChanged(val value: String) : TrackerDetailsEvent

    data class DescriptionSelected(val value: String) : TrackerDetailsEvent

    object ActivityClicked: TrackerDetailsEvent

    data class ActivitySelected(val id: Int) : TrackerDetailsEvent
}
