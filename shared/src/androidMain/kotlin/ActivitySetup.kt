
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.adeo.kviewmodel.odyssey.setupWithViewModels
import navigation.NavigationTree
import navigation.generateGraph
import ru.alexgladkov.odyssey.compose.base.Navigator
import ru.alexgladkov.odyssey.compose.extensions.setupWithActivity
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.ModalNavigator
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.configuration.DefaultModalConfiguration
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.compose.setup.StartScreen
import ru.alexgladkov.odyssey.core.configuration.DisplayType
import theme.AppTheme
import theme.Theme.colors

fun ComponentActivity.setupThemedNavigation() {

    setContent {
        AppTheme {

            val configuration = OdysseyConfiguration(
                canvas = this,
                backgroundColor = colors.primaryBackground,
                startScreen = StartScreen.Custom(NavigationTree.Splash.SplashScreen.name),
                statusBarColor = colors.primaryBackground.toArgb(),
                navigationBarColor = colors.primaryBackground.toArgb(),
                displayType = DisplayType.FullScreen
            )


            setNavigationContent(configuration = configuration) {
                generateGraph()
            }
        }
    }
}

// Copied from Odessey to use integration with view models
@Composable
private fun setNavigationContent(configuration: OdysseyConfiguration, navigationGraph: RootComposeBuilder.() -> Unit) {
    val rootController = RootComposeBuilder().apply(navigationGraph).build()
    rootController.backgroundColor = configuration.backgroundColor
    rootController.setupWithActivity(configuration.canvas)
    rootController.setupWithViewModels()
    rootController.onApplicationFinish = {}

    when (configuration.displayType) {
        is DisplayType.FullScreen -> {
            WindowCompat.setDecorFitsSystemWindows(configuration.canvas.window, false)
            configuration.canvas.window.statusBarColor = configuration.statusBarColor
            configuration.canvas.window.navigationBarColor = configuration.navigationBarColor
        }
        else -> {}
    }

    CompositionLocalProvider(
        LocalRootController provides rootController
    ) {
        ModalNavigator(configuration = DefaultModalConfiguration(configuration.backgroundColor, configuration.displayType)) {
            when (val startScreen = configuration.startScreen) {
                is StartScreen.Custom -> Navigator(startScreen = startScreen.startName)
                StartScreen.First -> Navigator(startScreen = rootController.getFirstScreenName())
            }
        }
    }
}
