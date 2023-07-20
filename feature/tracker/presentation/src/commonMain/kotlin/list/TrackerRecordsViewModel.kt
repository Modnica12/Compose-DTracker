package list

import TrackerRecordsRepository
import com.adeo.kviewmodel.BaseSharedViewModel
import di.getKoinInstance
import utils.formatDuration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import list.model.TrackerRecordsAction
import list.model.TrackerRecordsEvent
import list.model.TrackerRecordsScreenState
import list.model.TrackerRecordsState
import model.TrackerListItem
import model.TrackerRecord
import model.details.TrackerRecordDetails
import model.details.toDetails
import model.toDateGroups

internal class TrackerRecordsViewModel : BaseSharedViewModel<TrackerRecordsState, TrackerRecordsAction, TrackerRecordsEvent>(
    initialState = TrackerRecordsState()
) {

    private val repository: TrackerRecordsRepository = getKoinInstance()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getRecords().onSuccess { records ->
                val dateGroups = records.toDateGroups()
                viewState = viewState.copy(
                    screenState = TrackerRecordsScreenState.Idle,
                    dateGroups = dateGroups
                )
            }.onFailure { viewState = viewState.copy(screenState = TrackerRecordsScreenState.Error) }
        }
        viewModelScope.launch {
            repository.getCurrentRecord()
        }
        viewModelScope.launch {
            repository.currentRecord.collect { currentRecord ->
                currentRecord?.let { record ->
                    viewState = viewState.copy(currentRecord = record.toDetails())

                    // fix
                    timerJob?.cancel()
                    timerJob = null

                    startTracker(startDuration = record.duration)
                }
            }
        }
    }

    override fun obtainEvent(viewEvent: TrackerRecordsEvent) {
        when (viewEvent) {
            is TrackerRecordsEvent.TrackerButtonClicked -> trackerButtonClicked()
            is TrackerRecordsEvent.TaskGroupClicked -> taskGroupClicked(viewEvent.taskGroup)
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
        repository.currentRecord.value = TrackerRecord.default
        navigateToDetails()
        if (!viewState.currentRecord.isTracking) {
            startTracker()
        }
    }

    private fun bottomBarClicked() {
        navigateToDetails()
    }

    private fun navigateToDetails() {
        withViewModelScope {
            viewAction = TrackerRecordsAction.NavigateToDetails
            delay(100)
            viewAction = null
        }
    }

    private fun startTracker(startDuration: Int = 0) {
        timerJob = viewModelScope.launch {
            var duration = startDuration
            while (true) {
                val currentRecord = viewState.currentRecord
                viewState = viewState.copy(
                    currentRecord = currentRecord.copy(duration = duration.formatDuration())
                )
                duration += 1
                delay(1000)
            }
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
