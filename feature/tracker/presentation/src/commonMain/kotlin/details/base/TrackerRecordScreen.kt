package details.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import details.autocomplete.TrackerDetailsTextField.Description
import details.autocomplete.TrackerDetailsTextField.Project
import details.autocomplete.TrackerDetailsTextField.Task
import details.autocomplete.TrackerDetailsTextFieldSuggestion
import details.autocomplete.TrackerDetailsTextFieldSuggestion.*
import details.model.TrackerRecordAction
import details.model.TrackerRecordEvent.ActivitySelected
import details.model.TrackerRecordEvent.CloseClicked
import details.model.TrackerRecordEvent.CreateClicked
import details.model.TrackerRecordEvent.EndTimeChanged
import details.model.TrackerRecordEvent.SelectActivityClicked
import details.model.TrackerRecordEvent.StartTimeChanged
import details.model.TrackerRecordEvent.TextFieldSuggestionClicked
import details.model.TrackerRecordEvent.TextFieldValueChanged
import details.view.TrackerDetailsView
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.backpress.BackPressedCallback
import ru.alexgladkov.odyssey.core.backpress.OnBackPressedDispatcher
import utils.formatDuration

@Composable
internal fun TrackerRecordScreen(viewModel: TrackerRecordViewModel) {
    val rootController = LocalRootController.current
    val state by viewModel.viewStates().observeAsState()
    val textFieldsState by viewModel.textFieldsState.observeAsState()
    val duration by viewModel.durationFlow.observeAsState()
    val actions by viewModel.viewActions().observeAsState()
    TrackerDetailsView(
        project = textFieldsState.projectText,
        activity = state.selectedActivity?.name,
        task = textFieldsState.taskText,
        description = textFieldsState.descriptionText,
        start = state.startTime,
        end = state.endTime,
        duration = duration.formatDuration(),
        projectsSuggestions = textFieldsState.projectSuggestions,
        taskSuggestions = textFieldsState.taskSuggestions,
        descriptionSuggestions = textFieldsState.descriptionSuggestions,
        activitiesList = state.activitiesList,
        errorText = state.errorMessage,
        onProjectChange = { value -> viewModel.obtainEvent(TextFieldValueChanged(Project(value))) },
        onProjectSelect = { id -> viewModel.obtainEvent(TextFieldSuggestionClicked(Project(id))) },
        onTaskChange = { value -> viewModel.obtainEvent(TextFieldValueChanged(Task(value))) },
        onTaskSelect = { taskHint ->
            viewModel.obtainEvent(TextFieldSuggestionClicked(Task(taskHint)))
        },
        onDescriptionChange = { value ->
            viewModel.obtainEvent(TextFieldValueChanged(Description(value)))
        },
        onDescriptionSelect = { value ->
            viewModel.obtainEvent(
                TextFieldSuggestionClicked(TrackerDetailsTextFieldSuggestion.Description(value))
            )
        },
        onActivityClick = { viewModel.obtainEvent(SelectActivityClicked) },
        onActivitySelect = { id -> viewModel.obtainEvent(ActivitySelected(id)) },
        onStartTimeChange = { startTime -> viewModel.obtainEvent(StartTimeChanged(startTime)) },
        onEndTimeChange = { endTime -> viewModel.obtainEvent(EndTimeChanged(endTime)) },
        onCloseClick = { viewModel.obtainEvent(CloseClicked) },
        onCreateClick = { viewModel.obtainEvent(CreateClicked) }
    )

    actions?.let { action ->
        when (action) {
            is TrackerRecordAction.NavigateBack -> {
                rootController.popBackStack()
            }
        }
    }
    rootController.setupBackPressedDispatcher(
        onBackPressedDispatcher = OnBackPressedDispatcher().apply {
            backPressedCallback = object : BackPressedCallback() {
                override fun onBackPressed() {
                    viewModel.obtainEvent(CloseClicked)
                }
            }
        })
}
