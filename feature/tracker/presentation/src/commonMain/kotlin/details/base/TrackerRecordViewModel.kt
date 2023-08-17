package details.base

import TrackerRecordsRepository
import com.adeo.kviewmodel.BaseSharedViewModel
import details.autocomplete.AutoCompleteTextChangedHandler
import details.autocomplete.TrackerDetailsTextField
import details.autocomplete.TrackerDetailsTextFieldSuggestion
import details.model.TrackerRecordAction
import details.model.TrackerRecordEvent
import details.model.TrackerRecordViewState
import di.getKoinInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import model.TrackerRecord
import utils.detailsTimeToLocal
import utils.formatDetails
import kotlin.jvm.JvmStatic

internal abstract class TrackerRecordViewModel :
    BaseSharedViewModel<TrackerRecordViewState, TrackerRecordAction, TrackerRecordEvent>(
        initialState = TrackerRecordViewState()
    ) {

    // TODO: add usecases
    protected val repository: TrackerRecordsRepository = getKoinInstance()

    protected val mutableDurationFlow = MutableStateFlow(0)
    val durationFlow: StateFlow<Int> = mutableDurationFlow.asStateFlow()

    private val autoCompleteHandler = AutoCompleteTextChangedHandler(
        onFieldRequest = { textField ->
            when (textField) {
                is TrackerDetailsTextField.Project -> Unit
                is TrackerDetailsTextField.Task -> viewState =
                    viewState.copy(selectedTask = textField.text)

                is TrackerDetailsTextField.Description -> viewState =
                    viewState.copy(selectedDescription = textField.text)
            }
        },
        requestProjectSuggestions = { pattern ->
            repository.getProjects(key = pattern).getOrNull() ?: emptyList()
        },
        requestTaskSuggestion = { pattern ->
            val projectIds = viewState.selectedProject?.id ?: 0
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
            viewState = viewState.copy(selectedProject = project, errorMessage = null)
        },
        onTaskSelected = { task ->
            viewState = viewState.copy(selectedTask = task.name, selectedDescription = task.summary)
        },
        onDescriptionSelected = { description ->
            viewState = viewState.copy(selectedDescription = description)
        }
    )

    val textFieldsState = autoCompleteHandler.textFieldsState

    protected fun setRecordData(record: TrackerRecord) {
        with(record) {
            val taskName = task?.name ?: ""
            autoCompleteHandler.updateFieldsState {
                copy(
                    projectText = project?.key ?: "",
                    taskText = taskName,
                    descriptionText = description
                )
            }
            viewState = viewState.copy(
                selectedProject = project,
                selectedTask = taskName,
                selectedDescription = description,
                selectedActivity = activity,
                startTime = start.time.formatDetails()
            )
        }
    }

    override fun obtainEvent(viewEvent: TrackerRecordEvent) {
        when (viewEvent) {
            is TrackerRecordEvent.TextFieldValueChanged -> textFieldValueChanged(viewEvent.textField)
            is TrackerRecordEvent.TextFieldSuggestionClicked -> textFieldSuggestionClicked(
                viewEvent.suggestion
            )
            is TrackerRecordEvent.SelectActivityClicked -> selectActivityClicked()
            is TrackerRecordEvent.ActivitySelected -> activitySelected(viewEvent.id)
            is TrackerRecordEvent.StartTimeChanged -> startTimeChanged(viewEvent.value)
            is TrackerRecordEvent.EndTimeChanged -> endTimeChanged(viewEvent.value)
            is TrackerRecordEvent.CloseClicked -> closeClicked()
            is TrackerRecordEvent.CreateClicked -> createClicked()
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
                val startTimeInSeconds = LocalDateTime(
                    date = viewState.date,
                    time = startTime.detailsTimeToLocal()
                )
                    .toInstant(timeZone = TimeZone.currentSystemDefault())
                    .epochSeconds
                startTimeChanged(startTimeInSeconds = startTimeInSeconds)
            }
        }
    }

    protected abstract fun startTimeChanged(startTimeInSeconds: Long)

    private fun endTimeChanged(endTime: String) {
        if (endTime.length <= TIME_LENGTH && endTime.all { it.isDigit() }) {
            viewState = viewState.copy(endTime = endTime)
            if (endTime.length == TIME_LENGTH) {
                val endTimeInSeconds = LocalDateTime(
                    date = viewState.date,
                    time = endTime.detailsTimeToLocal()
                )
                    .toInstant(offset = UtcOffset.ZERO)
                    .epochSeconds
                endTimeChanged(endTimeInSeconds = endTimeInSeconds)
            }
        }
    }

    protected open fun endTimeChanged(endTimeInSeconds: Long) {}

    protected abstract suspend fun applyChanges(projectId: Int)

    private fun closeClicked() {
        viewAction = TrackerRecordAction.NavigateBack
    }

    private fun createClicked() {
        val projectId = viewState.selectedProject?.id
        if (projectId == null) {
            viewState = viewState.copy(errorMessage = "Заполните ключ проекта")
            return
        }
        viewModelScope.launch {
            applyChanges(projectId = projectId)
        }
            .invokeOnCompletion { viewAction = TrackerRecordAction.NavigateBack }
    }

    companion object {
        @JvmStatic
        protected val TIME_LENGTH = 4
    }
}
