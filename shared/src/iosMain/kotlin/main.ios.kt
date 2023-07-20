import androidx.compose.ui.window.ComposeUIViewController
import navigation.NavigationTree
import navigation.generateGraph
import platform.UIKit.UIViewController
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.compose.setup.StartScreen
import ru.alexgladkov.odyssey.compose.setup.setNavigationContent
import theme.AppTheme

fun MainViewController(): UIViewController =
    ComposeUIViewController {
        AppTheme {
            val odysseyConfiguration = OdysseyConfiguration(
                startScreen = StartScreen.Custom(NavigationTree.Tracker.List.name)
            )
            setNavigationContent(odysseyConfiguration) {
                generateGraph()
            }
        }
    }
