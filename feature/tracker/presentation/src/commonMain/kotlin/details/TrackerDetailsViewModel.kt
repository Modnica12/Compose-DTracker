package details

import TrackerRecordsRepository
import com.adeo.kviewmodel.BaseSharedViewModel
import details.model.TrackerDetailsAction
import details.model.TrackerDetailsEvent
import details.model.TrackerDetailsState
import di.getKoinInstance
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import model.TrackerTask
import model.TrackerTaskHint

@OptIn(FlowPreview::class)
class TrackerDetailsViewModel :
    BaseSharedViewModel<TrackerDetailsState, TrackerDetailsAction, TrackerDetailsEvent>(
        initialState = TrackerDetailsState()
    ) {

    private val projectTextFieldFlow = MutableStateFlow("")
    val projectFlow: StateFlow<String> = projectTextFieldFlow.asStateFlow()

    private val taskTextFieldFlow = MutableStateFlow("")

    private val descriptionTextFieldFlow = MutableStateFlow("")

    private val repository: TrackerRecordsRepository = getKoinInstance()

    init {
        viewModelScope.launch {
            repository.currentRecord.collect { currentRecord ->
                currentRecord?.apply {
                    val projectKey = project?.key ?: ""
                    val taskName = task?.name ?: ""
                    viewState = viewState.copy(
                        selectedProject = project,
                        selectedActivity = activity,
                        task = taskName,
                        description = description,
                        start = start,
                        duration = duration
                    )
                    projectTextFieldFlow.value = projectKey
                    taskTextFieldFlow.value = taskName
                    descriptionTextFieldFlow.value = description
                }
            }
        }
        projectTextFieldFlow.collectTextFieldValues { projectValue ->
            val projects = repository.getProjects(key = projectValue).getOrNull() ?: emptyList()
            viewState = viewState.copy(projectSuggestions = projects)
        }

        taskTextFieldFlow.collectTextFieldValues { taskValue ->
            val projectIds = repository.currentRecord.value?.project?.id ?: 0
            val tasks = repository.getTasks(
                projectIds = projectIds,
                pattern = taskValue
            )
                .getOrNull()
                ?: emptyList()
            viewState = viewState.copy(taskSuggestions = tasks)
        }

        descriptionTextFieldFlow.collectTextFieldValues { descriptionValue ->
            val descriptions = repository.getDescriptions(pattern = descriptionValue)
                .getOrNull()
                ?: emptyList()
            viewState = viewState.copy(descriptionSuggestions = descriptions)
        }

        repository.currentRecord.collectTextFieldValues {
            startTracker()
        }
    }

    override fun obtainEvent(viewEvent: TrackerDetailsEvent) {
        when (viewEvent) {
            is TrackerDetailsEvent.ProjectValueChanged -> projectValueChanged(viewEvent.value)
            is TrackerDetailsEvent.ProjectSelected -> projectSelected(viewEvent.id)
            is TrackerDetailsEvent.TaskValueChanged -> taskValueChanged(viewEvent.value)
            is TrackerDetailsEvent.TaskSelected -> taskSelected(viewEvent.taskHint)
            is TrackerDetailsEvent.DescriptionValueChanged -> descriptionValueChanged(viewEvent.value)
            is TrackerDetailsEvent.DescriptionSelected -> descriptionSelected(viewEvent.value)
            is TrackerDetailsEvent.ActivityClicked -> activityClicked()
            is TrackerDetailsEvent.ActivitySelected -> activitySelected(viewEvent.id)
        }
    }

    private fun projectValueChanged(value: String) {
        val errorMessage = if (value.isEmpty()) "Заполните ключ проекта" else null
        viewState = viewState.copy(/*project = value, */errorMessage = errorMessage)
        projectTextFieldFlow.value = value
    }

    private fun projectSelected(id: Int) {
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                viewState.projectSuggestions.firstOrNull { it.id == id }?.let { selectedProject ->
                    repository.currentRecord.value = currentRecord.copy(project = selectedProject)
                }
            }
        }
    }

    private fun taskValueChanged(value: String) {
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                val recordWithNewTask = currentRecord.copy(task = TrackerTask(name = value, onYoutrack = false))
                repository.currentRecord.value = recordWithNewTask
                taskTextFieldFlow.value = value
            }
        }
    }

    private fun taskSelected(taskHint: TrackerTaskHint) {
        val task = TrackerTask(name = taskHint.name, onYoutrack = false)
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                repository.currentRecord.value = currentRecord.copy(
                    task = task,
                    description = taskHint.summary
                )
            }
        }
    }

    private fun descriptionValueChanged(value: String) {
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                val recordWithNewDescription = currentRecord.copy(description = value)
                repository.currentRecord.value = recordWithNewDescription
                descriptionTextFieldFlow.value = value
            }
        }
    }

    private fun descriptionSelected(value: String) {
        withViewModelScope {
            repository.currentRecord.value?.let { currentRecord ->
                repository.currentRecord.value = currentRecord.copy(description = value)
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

    private fun <T> Flow<T>.collectTextFieldValues(onEach: suspend (T) -> Unit) {
        this
            .debounce(1000)
            .distinctUntilChanged()
            // do not trigger first initializing value set
            .drop(1)
            .onEach(onEach)
            .launchIn(viewModelScope)
    }
}
