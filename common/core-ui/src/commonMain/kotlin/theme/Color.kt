package theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class TrackerColors(
    val primaryBackground: Color,
    val primaryText: Color,
    val primaryContainerBackground: Color,
    val primaryContainerText: Color,
    val accentText: Color
)

internal val defaultPalette = TrackerColors(
    primaryBackground = Color(0xFFE2E2E2),
    primaryText = Color(0xFF080808),
    primaryContainerBackground = Color(0xFFFFFFFF),
    primaryContainerText = Color(0xFF0D0D0D),
    accentText = Color(0xFFC00000),
)

val LocalColorProvider = staticCompositionLocalOf<TrackerColors> { error("No default implementation") }
