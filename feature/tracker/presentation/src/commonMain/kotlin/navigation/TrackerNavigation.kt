package navigation

import TrackerRecordsScreen
import ru.alexgladkov.odyssey.compose.extensions.flow
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder

fun RootComposeBuilder.trackerFlow() {
    flow(NavigationTree.Tracker.TrackerFlow.name) {
        screen(NavigationTree.Tracker.List.name) {
            TrackerRecordsScreen()
        }
    }
}
