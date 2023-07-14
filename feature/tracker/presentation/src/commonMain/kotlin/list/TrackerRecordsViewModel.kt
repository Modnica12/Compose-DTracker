package list

import TrackerRecordsRepository
import com.adeo.kviewmodel.BaseSharedViewModel
import di.getKoinInstance
import formatDuration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import list.model.TrackerRecordsAction
import list.model.TrackerRecordsEvent
import list.model.TrackerRecordsState
import model.TrackerListItem
import model.details.TrackerRecordDetails
import model.details.toDetails
import model.toDateGroups

class TrackerRecordsViewModel : BaseSharedViewModel<TrackerRecordsState, TrackerRecordsAction, TrackerRecordsEvent>(
    initialState = TrackerRecordsState()
) {

    private val repository: TrackerRecordsRepository = getKoinInstance()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            val dateGroups = repository.getRecords().toDateGroups()
            viewState = viewState.copy(dateGroups = dateGroups)
        }
        viewModelScope.launch {
            repository.getCurrentRecord()
                .onSuccess { currentRecord ->
                    viewState = viewState.copy(currentRecord = currentRecord.toDetails())
                    startTracker(startDuration = currentRecord.duration)
                }
        }
    }

    override fun obtainEvent(viewEvent: TrackerRecordsEvent) {
        when (viewEvent) {
            is TrackerRecordsEvent.TrackerButtonClicked -> trackerButtonClicked()
            is TrackerRecordsEvent.TaskGroupClicked -> taskGroupClicked(viewEvent.taskGroup)
            is TrackerRecordsEvent.StartClicked -> startClicked()
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
        if (!viewState.currentRecord.isTracking) {
            startTracker()
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
