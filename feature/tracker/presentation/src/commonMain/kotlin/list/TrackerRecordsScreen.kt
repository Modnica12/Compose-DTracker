package list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import list.model.RecordDialogState
import list.model.TrackerRecordsAction
import list.model.TrackerRecordsEvent
import list.model.TrackerRecordsScreenState
import list.view.TrackerBottomBar
import list.view.TrackerRecordsErrorView
import list.view.TrackerRecordsLoadingView
import list.view.TrackerRecordsView
import navigation.NavigationTree
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes
import theme.Theme.typography
import utils.handleAction

@Composable
fun TrackerRecordsScreen() {
    val rootController = LocalRootController.current
    StoredViewModel(factory = { TrackerRecordsViewModel() }) { viewModel ->
        val state by viewModel.viewStates().observeAsState()
        val dialogState = state.recordDialogState
        val tracking = state.currentRecord.isTracking
        when (state.screenState) {
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
                        onRecordClick = { id ->
                            viewModel.obtainEvent(TrackerRecordsEvent.RecordClicked(recordId = id))
                        },
                        onRecordLongClick = { id ->
                            viewModel.obtainEvent(TrackerRecordsEvent.RecordLongClicked(recordId = id))
                        },
                        onTaskGroupClick = { taskGroup ->
                            viewModel.obtainEvent(TrackerRecordsEvent.TaskGroupClicked(taskGroup = taskGroup))
                        }
                    )
                }
            )

            TrackerRecordsScreenState.Loading -> TrackerRecordsLoadingView()
            TrackerRecordsScreenState.Error -> TrackerRecordsErrorView()
        }

        if (dialogState is RecordDialogState.Shown) {
            RecordActionsDialog(
                onDismissRequest = { viewModel.obtainEvent(TrackerRecordsEvent.DismissRecordDialog) },
                onRunClicked = {
                    viewModel.obtainEvent(TrackerRecordsEvent.RunRecordClicked(dialogState.recordId))
                }
            )
        }

        viewModel.handleAction {
            when (this) {
                is TrackerRecordsAction.NavigateToRecordEditor -> rootController.push(
                    screen = NavigationTree.Tracker.EditRecord.name,
                    params = recordId
                )

                is TrackerRecordsAction.NavigateToCurrentRecord -> rootController.present(
                    screen = NavigationTree.Tracker.CurrentRecord.name
                )

                is TrackerRecordsAction.NavigateToNewRecord -> rootController.present(
                    screen = NavigationTree.Tracker.NewRecord.name
                )
            }
        }
    }
}

@Composable
private fun TrackerButton(tracking: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = tracking,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        FloatingActionButton(
            backgroundColor = colors.accent,
            onClick = onClick
        ) {
            Box(
                modifier = Modifier
                    .size(dimens.medium)
                    .background(color = colors.primaryContainerBackground, shape = shapes.roundedSmall)
            )
        }
    }
}

@Composable
private fun RecordActionsDialog(
    onDismissRequest: () -> Unit,
    onRunClicked: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .padding(vertical = dimens.medium)
                .background(color = colors.primaryContainerBackground)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onRunClicked)
                    .padding(all = dimens.large),
                text = "Запустить",
                style = typography.bodyNormal,
                color = colors.onPrimaryContainerText
            )
        }
    }
}
