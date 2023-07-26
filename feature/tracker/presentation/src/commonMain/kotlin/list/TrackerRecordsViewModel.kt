package list

import TrackerRecordsRepository
import com.adeo.kviewmodel.BaseSharedViewModel
import di.getKoinInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import list.model.TrackerRecordsAction
import list.model.TrackerRecordsEvent
import list.model.TrackerRecordsScreenState
import list.model.TrackerRecordsState
import model.TrackerListItem
import model.details.TrackerRecordDetails
import model.details.toDetails
import model.toDateGroups
import usecase.StartTrackerTimerUseCase

internal class TrackerRecordsViewModel : BaseSharedViewModel<TrackerRecordsState, TrackerRecordsAction, TrackerRecordsEvent>(
    initialState = TrackerRecordsState()
) {

    private val repository: TrackerRecordsRepository = getKoinInstance()
    private val startTrackerTimerUseCase = StartTrackerTimerUseCase()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getRecords()
                .catch {
                    viewState = viewState.copy(screenState = TrackerRecordsScreenState.Error)
                }
                .collect { records ->
                    val dateGroups = records.toDateGroups()
                    viewState = viewState.copy(
                        screenState = TrackerRecordsScreenState.Idle,
                        dateGroups = dateGroups
                    )
                }
        }
        viewModelScope.launch {
            repository.getCurrentRecord()
        }
        viewModelScope.launch {
            repository.fetchRecords()
        }
        viewModelScope.launch {
            repository.currentRecord.collect { currentRecord ->
                currentRecord?.let { record ->
                    viewState = viewState.copy(currentRecord = record.toDetails())
                    startTracker(startDuration = record.duration)
                } ?: run {
                    viewState = viewState.copy(currentRecord = TrackerRecordDetails.default)
                }
            }
        }
    }

    override fun obtainEvent(viewEvent: TrackerRecordsEvent) {
        when (viewEvent) {
            is TrackerRecordsEvent.TrackerButtonClicked -> trackerButtonClicked()
            is TrackerRecordsEvent.TaskGroupClicked -> taskGroupClicked(viewEvent.taskGroup)
            is TrackerRecordsEvent.RecordClicked -> navigateToDetails(viewEvent.recordId)
            is TrackerRecordsEvent.StartClicked -> startClicked()
            is TrackerRecordsEvent.BottomBarClicked -> bottomBarClicked()
        }
    }

    private fun trackerButtonClicked() {
        if (viewState.currentRecord.isTracking) {
            stopTracker()
        }
    }

    private fun taskGroupClicked(taskGroup: TrackerListItem.TaskGroup) {
        viewState = viewState.copy(dateGroups = viewState.dateGroups.map { dateGroup ->
            if (dateGroup.date == taskGroup.date) {
                dateGroup.copy(items = dateGroup.items.map { item ->
                    item.transformIfTarget(taskGroup.name, taskGroup.project) { taskGroup ->
                        taskGroup.copy(isExpanded = !taskGroup.isExpanded)
                    }
                })
            } else dateGroup
        })
    }

    private fun TrackerListItem.transformIfTarget(
        taskName: String,
        taskProject: String,
        transform: (TrackerListItem.TaskGroup) -> TrackerListItem.TaskGroup
    ): TrackerListItem {
        return if (this is TrackerListItem.TaskGroup && name == taskName && taskProject == taskProject) {
            transform(this)
        } else this
    }

    private fun startClicked() {
        navigateToDetails()
    }

    private fun bottomBarClicked() {
        navigateToDetails()
    }

    private fun navigateToDetails(recordId: String? = null) {
        withViewModelScope {
            viewAction = TrackerRecordsAction.NavigateToDetails(recordId = recordId)
            delay(100)
            viewAction = null
        }
    }

    private fun startTracker(startDuration: Int) {
        timerJob?.cancel()
        timerJob = null
        timerJob = viewModelScope.launch {
            startTrackerTimerUseCase(startDuration)
                .onEach { duration ->
                    val currentRecord = repository.currentRecord.value
                    repository.currentRecord.value = currentRecord?.copy(duration = duration)
                }
                .collect()
        }
    }

    private fun stopTracker() {
        timerJob?.cancel()
        timerJob = null
        viewModelScope.launch {
            repository.stopTracker()
            viewState = viewState.copy(currentRecord = TrackerRecordDetails.default)
        }
    }
}
