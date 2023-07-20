package list.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import theme.Theme.colors
import theme.Theme.typography

@Composable
internal fun TrackerRecordsErrorView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Error", style = typography.headerLarge, color = colors.accent)
    }
}
