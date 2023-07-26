package components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup

/**
 * Copied from DropdownMenu()
 */
@Composable
internal fun PopUp(
    expanded: Boolean,
    offset: IntOffset = IntOffset.Zero,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = expanded

    if(expandedStates.currentState || expandedStates.targetState) {
        Popup(
            alignment = Alignment.TopCenter,
            offset = offset,
            onDismissRequest = onDismissRequest,
            content = content
        )
    }
}
