import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.adeo.kviewmodel.odyssey.setupWithViewModels
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import navigation.NavigationTree
import navigation.generateGraph
import ru.alexgladkov.odyssey.compose.base.Navigator
import ru.alexgladkov.odyssey.compose.extensions.setupWithActivity
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.ModalNavigator
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.configuration.DefaultModalConfiguration
import ru.alexgladkov.odyssey.core.configuration.DisplayType
import theme.AppTheme
import theme.Theme.colors

fun ComponentActivity.setupThemedNavigation() {
    val rootController = RootComposeBuilder().apply { generateGraph() }.build()
    rootController.setupWithActivity(this)
    rootController.setupWithViewModels()

    setContent {
        AppTheme {

            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(color = colors.primaryBackground)

            val configuration = DefaultModalConfiguration(
                backgroundColor = colors.primaryBackground,
                displayType = DisplayType.EdgeToEdge
            )

            CompositionLocalProvider(
                LocalRootController provides rootController
            ) {
                ModalNavigator(configuration = configuration) {
                    Navigator(startScreen = NavigationTree.Tracker.List.name)
                }
            }
        }
    }
}
