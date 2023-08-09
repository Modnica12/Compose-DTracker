
import di.getKoinInstance
import viewmodel.BaseViewModel

internal class AuthViewModel : BaseViewModel<AuthViewState, AuthAction, AuthEvent>(AuthViewState()) {

    private val authRepository: AuthRepository = getKoinInstance()

    override fun obtainEvent(viewEvent: AuthEvent) {
        when (viewEvent) {
            is AuthEvent.UserNameChanged -> viewState = viewState.copy(userName = viewEvent.userName)
            is AuthEvent.PasswordChanged -> viewState = viewState.copy(password = viewEvent.password)
            is AuthEvent.ChangePasswordVisibility -> viewState = viewState.copy(passwordVisible = !viewState.passwordVisible)
            is AuthEvent.LoginClicked -> {
                withViewModelScope {
                    authRepository.authenticate(
                        userName = viewState.userName,
                        password = viewState.password
                    ).onSuccess {
                        viewAction = AuthAction.NavigateToTracker
                    }
                }
            }
        }
    }
}

// TODO: move to separate files
internal data class AuthViewState(
    val userName: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
)

internal sealed interface AuthAction {

    object NavigateToTracker : AuthAction
}

internal sealed interface AuthEvent {

    data class UserNameChanged(val userName: String) : AuthEvent

    data class PasswordChanged(val password: String) : AuthEvent

    object ChangePasswordVisibility : AuthEvent

    object LoginClicked : AuthEvent
}
