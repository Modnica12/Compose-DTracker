package navigation

import AuthScreen
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder

fun RootComposeBuilder.authFlow() {
    screen(NavigationTree.Auth.Login.name) {
        AuthScreen()
    }
}
