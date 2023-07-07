package theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class TrackerDimens(
    val small: Dp,
    val default: Dp,
    val normal: Dp,
    val medium: Dp,
    val large: Dp,
    val extraLarge: Dp
)

val defaultDimens = TrackerDimens(
    small = 4.dp,
    default = 8.dp,
    normal = 12.dp,
    medium = 16.dp,
    large = 24.dp,
    extraLarge = 32.dp
)

val LocalDimenProvider = staticCompositionLocalOf<TrackerDimens> { error("No default implementation") }
