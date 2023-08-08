package viewmodel

import com.adeo.kviewmodel.BaseSharedViewModel

abstract class BaseViewModel<State : Any, Action, Event>(initialState: State) :
    BaseSharedViewModel<State, Action, Event>(initialState) {

    fun clearAction() {
        viewAction = null
    }
}
