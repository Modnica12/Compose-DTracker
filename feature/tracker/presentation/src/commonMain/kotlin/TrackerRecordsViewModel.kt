import com.adeo.kviewmodel.BaseSharedViewModel
import di.getKoinInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.TrackerDateGroup
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
            is Event.TrackerButtonClicked -> if (viewState.currentRecord.duration != null) {
                stopTracker()
            } else {
                startTracker()
            }
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

data class State(
    val dateGroups: List<TrackerDateGroup> = emptyList(),
    val currentRecord: TrackerRecordDetails = TrackerRecordDetails.default
)

sealed class Action

sealed interface Event {

    object TrackerButtonClicked : Event
}
