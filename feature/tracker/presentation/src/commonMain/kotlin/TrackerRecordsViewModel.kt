import com.adeo.kviewmodel.BaseSharedViewModel
import di.getKoinInstance
import kotlinx.coroutines.launch
import model.TrackerDateGroup
import model.toDateGroups

class TrackerRecordsViewModel: BaseSharedViewModel<State, Action, Event>(State()) {

    init {
        val repository: TrackerRecordsRepository = getKoinInstance()
        viewModelScope.launch {
            val dateGroups = repository.getRecords().toDateGroups()
            viewState = viewState.copy(dateGroups = dateGroups)
        }
    }

    override fun obtainEvent(viewEvent: Event) {

    }
}

data class State(
    val dateGroups: List<TrackerDateGroup> = emptyList()
)

sealed class Action

sealed class Event
