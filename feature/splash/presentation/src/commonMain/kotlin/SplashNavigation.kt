import navigation.NavigationTree
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder

fun RootComposeBuilder.splashFlow() {
    screen(NavigationTree.Splash.SplashScreen.name) {
        SplashScreen()
    }
}
