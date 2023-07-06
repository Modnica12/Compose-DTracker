import androidx.compose.runtime.Composable
import theme.AppTheme

@Composable
fun App() {
    AppTheme {
        TrackerRecordsScreen()
    }
}

expect fun getPlatformName(): String
