package details

import TrackerRecordsRepository
import com.adeo.kviewmodel.BaseSharedViewModel
import details.model.TrackerDetailsAction
import details.model.TrackerDetailsEvent
import details.model.TrackerDetailsState
import di.getKoinInstance
import kotlinx.coroutines.launch
import model.TrackerTask

class TrackerDetailsViewModel :
    BaseSharedViewModel<TrackerDetailsState, TrackerDetailsAction, TrackerDetailsEvent>(
        initialState = TrackerDetailsState()
    ) {

    private val repository: TrackerRecordsRepository = getKoinInstance()

    init {
        viewModelScope.launch {
            repository.currentRecord.collect { currentRecord ->
                currentRecord?.apply {
                    viewState = viewState.copy(
                        project = project?.key ?: "",
                        selectedProject = project,
                        selectedActivity = activity,
                        task = task?.name ?: "",
                        description = description,
                        start = start,
                        duration = duration
                    )
                }
            }
        }
    }

    override fun obtainEvent(viewEvent: TrackerDetailsEvent) {
        when (viewEvent) {
            is TrackerDetailsEvent.ProjectValueChanged -> projectValueChanged(viewEvent.value)
            is TrackerDetailsEvent.ProjectSelected -> projectSelected(viewEvent.id)
            is TrackerDetailsEvent.TaskValueChanged -> taskValueChanged(viewEvent.value)
            is TrackerDetailsEvent.DescriptionValueChanged -> descriptionValueChanged(viewEvent.value)
            is TrackerDetailsEvent.ActivityClicked -> activityClicked()
            is TrackerDetailsEvent.ActivitySelected -> activitySelected(viewEvent.id)
        }
    }

    private fun projectValueChanged(value: String) {
        val errorMessage = if (value.isEmpty()) "Заполните ключ проекта" else null
        viewState = viewState.copy(project = value, errorMessage = errorMessage)
        withViewModelScope {
            val projects = repository.getProjects(key = value).getOrNull() ?: emptyList()
            viewState = viewState.copy(projectsSuggestions = projects)
        }
    }

    private fun projectSelected(id: Int) {
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                viewState.projectsSuggestions.firstOrNull { it.id == id }?.let { selectedProject ->
                    repository.currentRecord.value = currentRecord.copy(project = selectedProject)
//                    viewState = viewState.copy(selectedProject = selectedProject)
                }
                startTracker()
            }
        }
    }

    private fun taskValueChanged(value: String) {
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                val recordWithNewTask = currentRecord.copy(task = TrackerTask(name = value, onYoutrack = false))
                repository.currentRecord.value = recordWithNewTask
                startTracker()
            }
        }
    }

    private fun descriptionValueChanged(value: String) {
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                val recordWithNewDescription = currentRecord.copy(description = value)
                repository.currentRecord.value = recordWithNewDescription
                startTracker()
            }
        }
    }

    private fun activityClicked() {
        withViewModelScope {
            val activities = repository.getActivities().getOrNull() ?: emptyList()
            viewState = viewState.copy(activitiesList = activities)
        }
    }

    private fun activitySelected(id: Int) {
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                viewState.activitiesList.firstOrNull { it.id == id }?.let { selectedActivity ->
                    repository.currentRecord.value = currentRecord.copy(activity = selectedActivity)
                }
                startTracker()
            }
        }
    }

    private suspend fun startTracker() {
        val currentRecord = repository.currentRecord.value
        currentRecord?.project?.let { project ->
            repository.startTracker(
                projectId = project.id,
                activityId = currentRecord.activity?.id,
                task = currentRecord.task?.name ?: "",
                description = currentRecord.description,
                start = currentRecord.start
            )
        } ?: run {
            viewState = viewState.copy(errorMessage = "Заполните ключ проекта")
        }
    }
}
