import androidx.compose.runtime.Composable
import list.TrackerRecordsScreen
import theme.AppTheme

@Composable
fun App() {
    AppTheme {
        TrackerRecordsScreen()
    }
}

expect fun getPlatformName(): String
