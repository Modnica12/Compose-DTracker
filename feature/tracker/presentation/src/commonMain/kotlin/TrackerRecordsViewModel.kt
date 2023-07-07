import com.adeo.kviewmodel.BaseSharedViewModel
import di.getKoinInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.TrackerDateGroup
import model.TrackerListItem
import model.TrackerRecordDetails
import model.toDateGroups
import model.toDetails

class TrackerRecordsViewModel : BaseSharedViewModel<State, Action, Event>(State()) {

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

    override fun obtainEvent(viewEvent: Event) {
        when (viewEvent) {
            is Event.TrackerButtonClicked -> trackerButtonClicked()
            is Event.TaskGroupClicked -> taskGroupClicked(viewEvent.taskGroup)
        }
    }

    private fun trackerButtonClicked() {
        if (viewState.currentRecord.duration != null) {
            stopTracker()
        } else {
            startTracker()
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

data class State(
    val dateGroups: List<TrackerDateGroup> = emptyList(),
    val currentRecord: TrackerRecordDetails = TrackerRecordDetails.default
)

sealed class Action

sealed interface Event {

    object TrackerButtonClicked : Event

    data class TaskGroupClicked(val taskGroup: TrackerListItem.TaskGroup) : Event
}
