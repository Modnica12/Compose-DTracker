package navigation

import details.currentRecord.CurrentRecordScreen
import details.edit.EditTrackerRecordScreen
import list.TrackerRecordsScreen
import ru.alexgladkov.odyssey.compose.extensions.flow
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder

fun RootComposeBuilder.trackerFlow() {
    flow(NavigationTree.Tracker.TrackerFlow.name) {
        screen(NavigationTree.Tracker.List.name) {
            TrackerRecordsScreen()
        }
        screen(NavigationTree.Tracker.NewRecord.name) {
            CurrentRecordScreen()
        }
        screen(NavigationTree.Tracker.CurrentRecord.name) {
            CurrentRecordScreen()
        }
        screen(NavigationTree.Tracker.EditRecord.name) { recordId ->
            EditTrackerRecordScreen(recordId = recordId as String)
        }
    }
}
