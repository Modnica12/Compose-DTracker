package details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import details.model.TrackerDetailsAction
import details.model.TrackerDetailsEvent
import details.view.TrackerDetailsView
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.backpress.BackPressedCallback
import ru.alexgladkov.odyssey.core.backpress.OnBackPressedDispatcher
import utils.formatDuration

@Composable
fun TrackerDetailsScreen(recordId: String? = null) {
    val rootController = LocalRootController.current
    StoredViewModel(factory = { TrackerDetailsViewModel(recordId) }) { viewModel ->
        val state by viewModel.viewStates().observeAsState()
        val duration by viewModel.durationFlow.observeAsState()
        val action by viewModel.viewActions().observeAsState()
        val project by viewModel.projectFlow.observeAsState()
        TrackerDetailsView(
            project = project,
            activity = state.selectedActivity?.name,
            task = state.task,
            description = state.description,
            start = state.startTime,
            end = state.endTime,
            duration = duration.formatDuration(),
            projectsSuggestions = state.projectSuggestions,
            taskSuggestions = state.taskSuggestions,
            descriptionSuggestions = state.descriptionSuggestions,
            activitiesList = state.activitiesList,
            errorText = state.errorMessage,
            onProjectChange = { value ->
                viewModel.obtainEvent(TrackerDetailsEvent.ProjectValueChanged(value))
            },
            onProjectSelect = { id ->
                viewModel.obtainEvent(TrackerDetailsEvent.ProjectSelected(id))
            },
            onTaskChange = { value ->
                viewModel.obtainEvent(TrackerDetailsEvent.TaskValueChanged(value))
            },
            onTaskSelect = { taskHint ->
                viewModel.obtainEvent(TrackerDetailsEvent.TaskSelected(taskHint))
            },
            onDescriptionChange = { value ->
                viewModel.obtainEvent(TrackerDetailsEvent.DescriptionValueChanged(value))
            },
            onDescriptionSelect = { value ->
                viewModel.obtainEvent(TrackerDetailsEvent.DescriptionSelected(value))
            },
            onActivityClick = { viewModel.obtainEvent(TrackerDetailsEvent.SelectActivityClicked)},
            onActivitySelect = { id ->
                viewModel.obtainEvent(TrackerDetailsEvent.ActivitySelected(id))
            },
            onStartTimeChange = { startTime ->
                viewModel.obtainEvent(TrackerDetailsEvent.StartTimeChanged(startTime))
            },
            onEndTimeChange = { endTime ->
                viewModel.obtainEvent(TrackerDetailsEvent.EndTimeChanged(endTime))
            },
            onCloseClick = { viewModel.obtainEvent(TrackerDetailsEvent.CloseClicked) },
            onCreateClick = { viewModel.obtainEvent(TrackerDetailsEvent.CreateClicked) }
        )

        action?.let { action ->
            when (action) {
                is TrackerDetailsAction.NavigateBack -> {
                    rootController.popBackStack()
                }
            }
        }
        rootController.setupBackPressedDispatcher(
            onBackPressedDispatcher = OnBackPressedDispatcher().apply {
                backPressedCallback = object : BackPressedCallback() {
                    override fun onBackPressed() {
                        viewModel.obtainEvent(TrackerDetailsEvent.CloseClicked)
                    }
                }
            })
    }
}
