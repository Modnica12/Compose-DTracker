import com.adeo.kviewmodel.BaseSharedViewModel
import di.getKoinInstance

internal class SplashViewModel: BaseSharedViewModel<SplashViewState, SplashAction, SplashEvent>(initialState = SplashViewState()) {

    private val authRepository: AuthRepository = getKoinInstance()

    init {
        withViewModelScope {
            viewAction = if (authRepository.isAuthenticated()) {
                SplashAction.NavigateToTracker
            } else {
                SplashAction.NavigateToAuth
            }
        }
    }

    override fun obtainEvent(viewEvent: SplashEvent) {
        // ignored
    }
}

// TODO: move to separate files

internal class SplashViewState

internal sealed interface SplashAction {

    object NavigateToAuth: SplashAction

    object NavigateToTracker: SplashAction
}

internal sealed interface SplashEvent
