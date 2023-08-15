package list

import TrackerRecordsRepository
import currentRecord.CurrentRecordManager
import di.getKoinInstance
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import list.model.TrackerRecordsAction
import list.model.TrackerRecordsEvent
import list.model.TrackerRecordsScreenState
import list.model.TrackerRecordsState
import model.TrackerListItem
import model.details.TrackerRecordDetails
import model.details.toDetails
import model.toDateGroups
import usecase.FetchRecordsUseCase
import viewmodel.BaseViewModel

internal class TrackerRecordsViewModel : BaseViewModel<TrackerRecordsState, TrackerRecordsAction, TrackerRecordsEvent>(
    initialState = TrackerRecordsState()
) {

    private val repository: TrackerRecordsRepository = getKoinInstance()
    private val fetchRecordsUseCase: FetchRecordsUseCase = getKoinInstance()
    private val currentRecordManager: CurrentRecordManager = getKoinInstance()


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
            repository.fetchCurrentRecord()
        }
        viewModelScope.launch {
            fetchRecordsUseCase()
        }
        viewModelScope.launch {
            currentRecordManager.currentRecord.collectLatest { currentRecord ->
                currentRecord?.let { record ->
                    viewState = viewState.copy(currentRecord = record.toDetails())
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
        // TODO: invokeOnCompletion попробовать
        viewAction = TrackerRecordsAction.NavigateToDetails(recordId = recordId)
    }

    private fun stopTracker() {
        viewModelScope.launch {
            currentRecordManager.stopTimer()
        }
    }
}
