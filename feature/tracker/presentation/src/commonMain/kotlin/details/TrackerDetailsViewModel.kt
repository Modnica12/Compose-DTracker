package details

import TrackerRecordsRepository
import com.adeo.kviewmodel.BaseSharedViewModel
import currentRecord.CurrentRecordManager
import details.autocomplete.AutoCompleteTextChangedHandler
import details.autocomplete.TrackerDetailsTextField
import details.autocomplete.TrackerDetailsTextFieldSuggestion
import details.model.TrackerDetailsAction
import details.model.TrackerDetailsEvent
import details.model.TrackerDetailsState
import di.getKoinInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import model.TrackerRecord
import usecase.StartTrackerTimerUseCase
import utils.addDuration
import utils.detailsTimeToLocal
import utils.formatDetails
import utils.toUTC

internal class TrackerDetailsViewModel(
    private val recordId: String?
) : BaseSharedViewModel<TrackerDetailsState, TrackerDetailsAction, TrackerDetailsEvent>(
    initialState = TrackerDetailsState()
) {

    private val mutableDurationFlow = MutableStateFlow(0)
    val durationFlow: StateFlow<Int> = mutableDurationFlow.asStateFlow()

    // TODO: add usecases
    private val repository: TrackerRecordsRepository = getKoinInstance()
    private val currentRecordManager: CurrentRecordManager = getKoinInstance()
    private val startTrackerTimerUseCase = StartTrackerTimerUseCase()

    private val autoCompleteHandler = AutoCompleteTextChangedHandler(
        onFieldRequest = { textField ->
            when (textField) {
                is TrackerDetailsTextField.Project -> Unit
                is TrackerDetailsTextField.Task -> viewState = viewState.copy(selectedTask = textField.text)
                is TrackerDetailsTextField.Description -> viewState = viewState.copy(selectedDescription = textField.text)
            }
        },
        requestProjectSuggestions = { pattern ->
            repository.getProjects(key = pattern).getOrNull() ?: emptyList()
        },
        requestTaskSuggestion = { pattern ->
            val projectIds = currentRecordManager.currentRecord.value?.project?.id ?: 0
            repository.getTasks(projectIds = projectIds, pattern = pattern)
                .getOrNull()
                ?: emptyList()
        },
        requestDescriptionSuggestion = { pattern ->
            repository.getDescriptions(pattern = pattern)
                .getOrNull()
                ?: emptyList()
        },
        onProjectSelected = { project ->
            viewState = viewState.copy(selectedProject = project)
        },
        onTaskSelected = { task ->
            viewState = viewState.copy(selectedTask = task.name, selectedDescription = task.summary)
        },
        onDescriptionSelected = { description ->
            viewState = viewState.copy(selectedDescription = description)
        }
    )

    val textFieldsState = autoCompleteHandler.textFieldsState

    private var timerJob: Job? = null

    init {
        withViewModelScope {
            if (recordId != null) {
                repository.getRecordWithId(recordId)?.setData()
            } else if (currentRecordManager.currentRecord.value == null) {
                startTimer(startDuration = 0)
            } else {
                repository.currentRecord.value?.apply {
                    setData()
                    startTimer(startDuration = duration)
                }
            }
        }
    }

    private fun TrackerRecord.setData() {
        val taskName = task?.name ?: ""
        autoCompleteHandler.updateFieldsState {
            copy(
                projectText = project?.key ?: "",
                taskText = taskName,
                descriptionText = description
            )
        }
        val endTime = if (recordId != null) start.addDuration(duration).time.formatDetails() else null
        viewState = viewState.copy(
            selectedProject = project,
            selectedTask = taskName,
            selectedDescription = description,
            selectedActivity = activity,
            startTime = start.time.formatDetails(),
            endTime = endTime
        )
    }

    override fun obtainEvent(viewEvent: TrackerDetailsEvent) {
        when (viewEvent) {
            is TrackerDetailsEvent.TextFieldValueChanged -> textFieldValueChanged(viewEvent.textField)
            is TrackerDetailsEvent.TextFieldSuggestionClicked -> textFieldSuggestionClicked(
                viewEvent.suggestion
            )

            is TrackerDetailsEvent.SelectActivityClicked -> selectActivityClicked()
            is TrackerDetailsEvent.ActivitySelected -> activitySelected(viewEvent.id)
            is TrackerDetailsEvent.StartTimeChanged -> startTimeChanged(viewEvent.value)
            is TrackerDetailsEvent.EndTimeChanged -> endTimeChanged(viewEvent.value)
            TrackerDetailsEvent.CloseClicked -> closeClicked()
            TrackerDetailsEvent.CreateClicked -> createClicked()
        }
    }

    private fun textFieldValueChanged(textField: TrackerDetailsTextField) {
        autoCompleteHandler.handleTextChange(textField)
    }

    private fun textFieldSuggestionClicked(suggestion: TrackerDetailsTextFieldSuggestion) {
        autoCompleteHandler.handleSuggestionClick(suggestion)
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
        if (startTime.length <= TIME_LENGTH && startTime.all { it.isDigit() }) {
            viewState = viewState.copy(startTime = startTime)
            if (startTime.length == TIME_LENGTH) {
                val startTimeSeconds = LocalDateTime(
                    date = viewState.date,
                    time = startTime.detailsTimeToLocal()
                )
                    .toInstant(timeZone = TimeZone.currentSystemDefault())
                    .epochSeconds
                val endTime = viewState.endTime
                if (recordId != null && endTime != null) {
                    val endTimeInSeconds = LocalDateTime(
                        date = viewState.date,
                        time = endTime.detailsTimeToLocal()
                    )
                        .toInstant(timeZone = TimeZone.currentSystemDefault())
                        .epochSeconds
                    if (endTimeInSeconds > startTimeSeconds) {
                        val diff = endTimeInSeconds - startTimeSeconds
                        mutableDurationFlow.value = diff.toInt()
                    }
                } else {
                    val now = Clock.System.now()

                    if (now.epochSeconds > startTimeSeconds) {
                        val diff = now.epochSeconds - startTimeSeconds
                        startTimer(startDuration = diff.toInt())
                    }
                }
            }
        }
    }

    private fun endTimeChanged(endTime: String) {
        if (endTime.length <= TIME_LENGTH && endTime.all { it.isDigit() }) {
            viewState = viewState.copy(endTime = endTime)

            if (endTime.length == TIME_LENGTH && recordId != null) {
                val startTimeSeconds = LocalDateTime(
                    date = viewState.date,
                    time = viewState.startTime.detailsTimeToLocal()
                )
                    .toInstant(offset = UtcOffset.ZERO)
                    .epochSeconds
                val endTimeInSeconds = LocalDateTime(
                    date = viewState.date,
                    time = endTime.detailsTimeToLocal()
                )
                    .toInstant(offset = UtcOffset.ZERO)
                    .epochSeconds
                if (endTimeInSeconds > startTimeSeconds) {
                    val diff = endTimeInSeconds - startTimeSeconds
                    mutableDurationFlow.value = diff.toInt()
                }
            }
        }
    }

    private fun saveChanges() {
        println(viewState)
        viewState.apply {
            val projectId = selectedProject?.id
            if (projectId == null) {
                viewState = viewState.copy(errorMessage = "Заполните ключ проекта")
                return@apply
            }

            currentRecordManager.updateRecord(
                projectId = projectId,
                activityId = selectedActivity?.id,
                task = selectedTask,
                description = selectedDescription,
                // Mapping from user's timezone
                start = LocalDateTime(date = date, time = startTime.detailsTimeToLocal()).toUTC()
            )
        }
    }

    private fun closeClicked() {
        viewAction = TrackerDetailsAction.NavigateBack
    }

    private fun createClicked() {
        viewModelScope.launch {
            if (recordId == null) {
                saveChanges()
            } else {
                // TODO: update record
            }
        }
            .invokeOnCompletion { viewAction = TrackerDetailsAction.NavigateBack }
    }

    private fun startTimer(startDuration: Int) {
        timerJob?.cancel()
        timerJob = null
        timerJob = viewModelScope.launch {
            startTrackerTimerUseCase(startDuration = startDuration)
                .collect { duration ->
                    ensureActive()
                    mutableDurationFlow.value = duration
                }
        }
    }

    companion object {
        private const val TIME_LENGTH = 4
    }
}
