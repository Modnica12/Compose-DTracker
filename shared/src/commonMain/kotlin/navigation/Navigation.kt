package navigation

import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import splashFlow

fun RootComposeBuilder.generateGraph() {
    splashFlow()
    authFlow()
    trackerFlow()
}
