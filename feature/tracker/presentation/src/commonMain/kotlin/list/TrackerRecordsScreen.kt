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
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import list.model.TrackerRecordsAction
import list.model.TrackerRecordsEvent
import list.model.TrackerRecordsScreenState
import list.view.TrackerBottomBar
import list.view.TrackerRecordsErrorView
import list.view.TrackerRecordsLoadingView
import list.view.TrackerRecordsView
import navigation.NavigationTree
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes

@Composable
fun TrackerRecordsScreen() {
    val rootController = LocalRootController.current
    StoredViewModel(factory = { TrackerRecordsViewModel() }) { viewModel ->
        val state by viewModel.viewStates().observeAsState()
        val action = viewModel.viewActions().observeAsState()
        val tracking = state.currentRecord.isTracking
        when(state.screenState) {
            TrackerRecordsScreenState.Idle -> Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colors.primaryBackground),
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
                        onClick = { viewModel.obtainEvent(TrackerRecordsEvent.BottomBarClicked) },
                        onStartClick = { viewModel.obtainEvent(TrackerRecordsEvent.StartClicked) })
                },
                content = { paddingValues ->
                    TrackerRecordsView(
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                        recordsItems = state.dateGroups,
                        onTaskGroupClick = {
                            viewModel.obtainEvent(TrackerRecordsEvent.TaskGroupClicked(taskGroup = it))
                        }
                    )
                }
            )
            TrackerRecordsScreenState.Loading -> TrackerRecordsLoadingView()
            TrackerRecordsScreenState.Error -> TrackerRecordsErrorView()
        }

        action.value?.let { action ->
            when (action) {
                is TrackerRecordsAction.NavigateToDetails -> {
                    rootController.present(NavigationTree.Tracker.Details.name)
                }
            }
        }
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
