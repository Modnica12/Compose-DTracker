import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import navigation.NavigationTree
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.animations.defaultFadeAnimation

@Composable
fun SplashScreen() {
    val rootController = LocalRootController.current
    StoredViewModel(factory = { SplashViewModel() }) { viewModel ->
        val action by viewModel.viewActions().observeAsState()
        SplashView()

        action?.let { splashAction ->
            when (splashAction) {
                SplashAction.NavigateToAuth -> rootController.launch(
                    screen = NavigationTree.Auth.Login.name,
                    animationType = defaultFadeAnimation()
                )

                SplashAction.NavigateToTracker -> rootController.launch(
                    screen = NavigationTree.Tracker.TrackerFlow.name,
                    animationType = defaultFadeAnimation()
                )
            }
        }
    }
}

@Composable
fun SplashView() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}
