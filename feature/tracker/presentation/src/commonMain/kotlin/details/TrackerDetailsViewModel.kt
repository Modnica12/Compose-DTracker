package details

import TrackerRecordsRepository
import com.adeo.kviewmodel.BaseSharedViewModel
import details.model.TrackerDetailsAction
import details.model.TrackerDetailsEvent
import details.model.TrackerDetailsState
import di.getKoinInstance
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import model.TrackerRecord
import model.TrackerTask
import model.TrackerTaskHint
import usecase.StartTrackerTimerUseCase
import utils.addDuration
import utils.detailsTimeToLocal
import utils.formatDetails

@OptIn(FlowPreview::class)
class TrackerDetailsViewModel(
    private val recordId: String?
) : BaseSharedViewModel<TrackerDetailsState, TrackerDetailsAction, TrackerDetailsEvent>(
    initialState = TrackerDetailsState()
) {

    private val mutableDurationFlow = MutableStateFlow(0)
    val durationFlow: StateFlow<Int> = mutableDurationFlow.asStateFlow()

    private val projectTextFieldFlow = MutableStateFlow("")
    val projectFlow: StateFlow<String> = projectTextFieldFlow.asStateFlow()

    private val taskTextFieldFlow = MutableStateFlow("")

    private val descriptionTextFieldFlow = MutableStateFlow("")

    private val repository: TrackerRecordsRepository = getKoinInstance()
    private val startTrackerTimerUseCase = StartTrackerTimerUseCase()

    private var timerJob: Job? = null

    init {
        initProjectTextField()
        initTaskTextField()
        initDescriptionTextField()

        withViewModelScope {
            if (recordId != null) {
                repository.getRecordWithId(recordId)?.setData()
            } else if (repository.currentRecord.value == null) {
                startTimer(startDuration = 0)
            } else {
                repository.currentRecord.value?.setData()
                repository.currentRecord.collect { currentRecord ->
                    currentRecord?.apply {
                        startTimer(startDuration = duration)
                    }
                }
            }
        }
    }

    private fun initProjectTextField() {
        projectTextFieldFlow.collectTextFieldValues { projectValue ->
            val trimmedProject = projectValue.trim()
            val projects = repository.getProjects(key = trimmedProject).getOrNull() ?: emptyList()
            viewState = viewState.copy(projectSuggestions = projects)
        }
    }

    private fun initTaskTextField() {
        taskTextFieldFlow.collectTextFieldValues { taskValue ->
            val trimmedTask = taskValue.trim()
            val projectIds = repository.currentRecord.value?.project?.id ?: 0
            val tasks = repository.getTasks(
                projectIds = projectIds,
                pattern = trimmedTask
            )
                .getOrNull()
                ?: emptyList()
            viewState = viewState.copy(taskSuggestions = tasks)
        }
    }

    private fun initDescriptionTextField() {
        descriptionTextFieldFlow.collectTextFieldValues { descriptionValue ->
            val trimmedDescription = descriptionValue.trim()
            val descriptions = repository.getDescriptions(pattern = trimmedDescription)
                .getOrNull()
                ?: emptyList()
            viewState = viewState.copy(descriptionSuggestions = descriptions)
        }
    }

    private fun TrackerRecord.setData() {
        mutableDurationFlow.value = duration
        projectTextFieldFlow.value = project?.key ?: ""
        taskTextFieldFlow.value = task?.name ?: ""
        val endTime = if (recordId != null) start.addDuration(duration).time.formatDetails() else null
        viewState = viewState.copy(
            selectedProject = project,
            task = task?.name ?: "",
            description = description,
            selectedActivity = activity,
            startTime = start.time.formatDetails(),
            endTime = endTime
        )
    }

    override fun obtainEvent(viewEvent: TrackerDetailsEvent) {
        when (viewEvent) {
            is TrackerDetailsEvent.ProjectValueChanged -> projectValueChanged(viewEvent.value)
            is TrackerDetailsEvent.ProjectSelected -> projectSelected(viewEvent.id)
            is TrackerDetailsEvent.TaskValueChanged -> taskValueChanged(viewEvent.value)
            is TrackerDetailsEvent.TaskSelected -> taskSelected(viewEvent.taskHint)
            is TrackerDetailsEvent.DescriptionValueChanged -> descriptionValueChanged(viewEvent.value)
            is TrackerDetailsEvent.DescriptionSelected -> descriptionSelected(viewEvent.value)
            is TrackerDetailsEvent.SelectActivityClicked -> selectActivityClicked()
            is TrackerDetailsEvent.ActivitySelected -> activitySelected(viewEvent.id)
            is TrackerDetailsEvent.StartTimeChanged -> startTimeChanged(viewEvent.value)
            is TrackerDetailsEvent.EndTimeChanged -> endTimeChanged(viewEvent.value)
            TrackerDetailsEvent.CloseClicked -> closeClicked()
            TrackerDetailsEvent.CreateClicked -> createClicked()
        }
    }

    private fun projectValueChanged(value: String) {
        val errorMessage = if (value.isEmpty()) "Заполните ключ проекта" else null
        viewState = viewState.copy(errorMessage = errorMessage)
        projectTextFieldFlow.value = value
    }

    private fun projectSelected(id: Int) {
        withViewModelScope {
            viewState.projectSuggestions.firstOrNull { it.id == id }?.let { selectedProject ->
                viewState = viewState.copy(selectedProject = selectedProject)
                // улетает лишний запрос
                projectTextFieldFlow.value = selectedProject.key
            }
        }
    }

    private fun taskValueChanged(value: String) {
        setTask(newTask = value)
    }

    private fun taskSelected(taskHint: TrackerTaskHint) {
        setTask(newTask = taskHint.name)
        setDescription(newDescription = taskHint.summary)
    }

    private fun setTask(newTask: String) {
        viewState = viewState.copy(task = newTask)
        taskTextFieldFlow.value = newTask
    }

    private fun descriptionValueChanged(value: String) {
        setDescription(newDescription = value)
    }

    private fun descriptionSelected(value: String) {
        setDescription(newDescription = value)
    }

    private fun setDescription(newDescription: String) {
        viewState = viewState.copy(description = newDescription)
        descriptionTextFieldFlow.value = newDescription
    }

    private fun selectActivityClicked() {
        withViewModelScope {
            val activities = repository.getActivities().getOrNull() ?: emptyList()
            viewState = viewState.copy(activitiesList = activities)
        }
    }

    private fun activitySelected(id: Int) {
        withViewModelScope {
            viewState.activitiesList.firstOrNull { it.id == id }?.let { selectedActivity ->
                viewState = viewState.copy(selectedActivity = selectedActivity)
            }
        }
    }

    private fun startTimeChanged(startTime: String) {
        if (startTime.length <= 4) {
            viewState = viewState.copy(startTime = startTime)
        }
    }

    private fun endTimeChanged(endTime: String) {
        if (endTime.length <= 4) {
            viewState = viewState.copy(endTime = endTime)
        }
    }

    private suspend fun startTracker() {
        viewState.apply {
            val projectId = selectedProject?.id
            if (projectId == null) {
                viewState = viewState.copy(errorMessage = "Заполните ключ проекта")
                return@apply
            }

            repository.startTracker(
                projectId = projectId,
                activityId = selectedActivity?.id,
                task = task,
                description = description,
                start = LocalDateTime(date = date, time = startTime.detailsTimeToLocal()).toString() + "Z"
            )
        }
    }

    private fun closeClicked() {
        viewAction = TrackerDetailsAction.NavigateBack
    }

    private fun createClicked() {
        viewModelScope.launch {
            if (recordId == null) {
                startTracker()
                repository.currentRecord.value = TrackerRecord(
                    id = "",
                    project = viewState.selectedProject,
                    activity = viewState.selectedActivity,
                    task = TrackerTask(name = viewState.task, onYoutrack = false),
                    start = LocalDateTime(date = viewState.date, time = viewState.startTime.detailsTimeToLocal()),
                    duration = durationFlow.value,
                    description = viewState.description
                )
            } else {
                // update record
            }
        }
            .invokeOnCompletion { viewAction = TrackerDetailsAction.NavigateBack }
    }

    private fun startTimer(startDuration: Int) {
        timerJob?.cancel()
        timerJob = null
        timerJob = viewModelScope.launch {
            startTrackerTimerUseCase(startDuration)
                .onEach { duration ->
                    mutableDurationFlow.value = duration
                }
                .collect()
        }
    }

    private fun <T> Flow<T>.collectTextFieldValues(onEach: suspend (T) -> Unit) {
        this
            .debounce(1000)
            .distinctUntilChanged()
            // Don't trigger first initializing value set
            .drop(1)
            .onEach(onEach)
            .launchIn(viewModelScope)
    }
}
