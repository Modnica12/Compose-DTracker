package list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.adeo.kviewmodel.compose.ViewModel
import com.adeo.kviewmodel.compose.observeAsState
import list.model.TrackerRecordsEvent
import list.view.TrackerBottomBar
import list.view.TrackerRecordsView
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.ModalSheetConfiguration
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes
import list.view.TrackerBottomBar
import view.TrackerDetailsView
import list.view.TrackerRecordsView

@Composable
fun TrackerRecordsScreen() {
    val rootController = LocalRootController.current
    val modalController = rootController.findModalController()
    ViewModel(factory = { TrackerRecordsViewModel() }) { viewModel ->
        val state by viewModel.viewStates().observeAsState()
        val tracking = state.currentRecord.isTracking
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                TrackerButton(
                    tracking = tracking,
                    onClick = { viewModel.obtainEvent(TrackerRecordsEvent.TrackerButtonClicked) }
                )
            },
            bottomBar = {
                TrackerBottomBar(
                    recordDetails = state.currentRecord,
                    tracking = tracking,
                    onStartClick = { viewModel.obtainEvent(TrackerRecordsEvent.StartClicked) })
            },
            content = { paddingValues ->
                TrackerRecordsView(
                    modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                    recordsItems = state.dateGroups,
                    onTaskGroupClick = { viewModel.obtainEvent(TrackerRecordsEvent.TaskGroupClicked(taskGroup = it)) }
                )
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TrackerButton(tracking: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = tracking,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        FloatingActionButton(
            backgroundColor = colors.primaryContainerBackground,
            onClick = onClick
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.medium)
                    .background(color = colors.accent, shape = shapes.roundedSmall)
            )
        }
    }
}