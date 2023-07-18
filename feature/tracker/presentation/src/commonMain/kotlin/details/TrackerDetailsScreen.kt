package details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import details.model.TrackerDetailsEvent
import details.view.TrackerDetailsView

@Composable
fun TrackerDetailsScreen() {
    StoredViewModel(factory = { TrackerDetailsViewModel() }) { viewModel ->
        val state by viewModel.viewStates().observeAsState()
        val project by viewModel.projectFlow.observeAsState()
        TrackerDetailsView(
            project = project,
            activity = state.selectedActivity?.name,
            task = state.task,
            description = state.description,
            start = state.start,
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
            onActivityClick = { viewModel.obtainEvent(TrackerDetailsEvent.ActivityClicked)},
            onActivitySelect = { id ->
                viewModel.obtainEvent(TrackerDetailsEvent.ActivitySelected(id))
            }
        )
    }
}
