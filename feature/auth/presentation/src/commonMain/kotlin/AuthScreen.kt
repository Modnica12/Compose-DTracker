import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import navigation.NavigationTree
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.animations.defaultFadeAnimation
import utils.handleAction

@Composable
fun AuthScreen() {
    val rootController = LocalRootController.current
    StoredViewModel(factory = { AuthViewModel() }) { viewModel ->
        val state by viewModel.viewStates().observeAsState()
        AuthView(
            userName = state.userName,
            password = state.password,
            passwordVisible = state.passwordVisible,
            onUserNameChange = { userName -> viewModel.obtainEvent(AuthEvent.UserNameChanged(userName = userName)) },
            onPasswordChange = { password -> viewModel.obtainEvent(AuthEvent.PasswordChanged(password = password)) },
            onPasswordVisibilityClick = { viewModel.obtainEvent(AuthEvent.ChangePasswordVisibility) },
            onLoginClick = { viewModel.obtainEvent(AuthEvent.LoginClicked) }
        )

        viewModel.handleAction {
            when (this) {
                // TODO: при навигации на экран трекера мы не авторизованы, только после перезахода все норм
                AuthAction.NavigateToTracker -> rootController.launch(
                    screen = NavigationTree.Tracker.TrackerFlow.name,
                    animationType = defaultFadeAnimation()
                )
            }
        }
    }
}
