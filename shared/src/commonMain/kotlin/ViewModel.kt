import com.adeo.kviewmodel.BaseSharedViewModel
import kotlinx.coroutines.launch

class ViewModel: BaseSharedViewModel<State, Action, Event>(State()) {

    init {
        val repository: TrackerRecordsRepository = getKoinInstance()
        viewModelScope.launch {
            repository.getRecords()
        }
    }

    override fun obtainEvent(viewEvent: Event) {

    }


}

class State

sealed class Action

sealed class Event
