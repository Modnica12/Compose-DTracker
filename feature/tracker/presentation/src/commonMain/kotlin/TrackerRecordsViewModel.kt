import com.adeo.kviewmodel.BaseSharedViewModel
import di.getKoinInstance
import kotlinx.coroutines.launch
import model.TrackerRecord
import model.TrackerRecordItem
import model.toPresentation

class TrackerRecordsViewModel: BaseSharedViewModel<State, Action, Event>(State()) {

    init {
        val repository: TrackerRecordsRepository = getKoinInstance()
        viewModelScope.launch {
            val records = repository.getRecords().map(TrackerRecord::toPresentation)
            viewState = viewState.copy(records = records)
        }
    }

    override fun obtainEvent(viewEvent: Event) {

    }
}

data class State(
    val records: List<TrackerRecordItem> = emptyList()
)

sealed class Action

sealed class Event
