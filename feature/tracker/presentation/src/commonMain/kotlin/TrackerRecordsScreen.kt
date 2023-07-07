import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.adeo.kviewmodel.compose.ViewModel
import com.adeo.kviewmodel.compose.observeAsState
import theme.Theme.colors
import theme.Theme.dimens
import view.TrackerBar
import view.TrackerRecordsView

@Composable
fun TrackerRecordsScreen() {
    ViewModel(factory = { TrackerRecordsViewModel() }) { viewModel ->
        val state by viewModel.viewStates().observeAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButtonPosition = FabPosition.End,
            isFloatingActionButtonDocked = true,
            bottomBar = { TrackerBar(recordDetails = state.currentRecord) },
            floatingActionButton = {
                TrackerButton(
                    isTracking = state.currentRecord.duration != null,
                    onClick = { viewModel.obtainEvent(Event.TrackerButtonClicked) }
                )
            },
            content = {
                TrackerRecordsView(
                    modifier = Modifier.padding(bottom = it.calculateBottomPadding() - dimens.default),
                    recordsItems = state.dateGroups
                )
            }
        )
    }
}

@Composable
private fun TrackerButton(isTracking: Boolean, onClick: () -> Unit) {
    FloatingActionButton(backgroundColor = colors.accentBackground, onClick = onClick) {
        Crossfade(targetState = isTracking) {
            Icon(
                imageVector = if (isTracking) Icons.Default.Done else Icons.Default.PlayArrow,
                tint = colors.onAccent,
                contentDescription = "start or stop tracker"
            )
        }
    }
}
