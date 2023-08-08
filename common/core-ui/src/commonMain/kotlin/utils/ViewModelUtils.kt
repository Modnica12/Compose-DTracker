package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.adeo.kviewmodel.compose.observeAsState
import kotlinx.coroutines.launch
import viewmodel.BaseViewModel

@Composable
fun <Action> BaseViewModel<*, Action, *>.handleAction(handler: Action.() -> Unit) {
    val scope = rememberCoroutineScope()
    val action by viewActions().observeAsState()
    action?.apply {
        scope
            .launch { handler() }
            .invokeOnCompletion { clearAction() }
    }
}
