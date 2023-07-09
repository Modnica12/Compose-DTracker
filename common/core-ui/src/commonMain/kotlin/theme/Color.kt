package theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class TrackerColors(
    val primaryBackground: Color,
    val onPrimaryText: Color,
    val primaryContainerBackground: Color,
    val primaryContainerBorder: Color,
    val onPrimaryContainerText: Color,
    val accent: Color,
    val accentContainer: Color
)

internal val defaultPalette = TrackerColors(
    primaryBackground = Color(0xFFE2E2E2),
    onPrimaryText = Color(0xFF080808),
    primaryContainerBackground = Color(0xFFF2F2F2),
    primaryContainerBorder = Color(0xFFD2D2D2),
    onPrimaryContainerText = Color(0xFF0D0D0D),
    accent = Color(0xFFD00000),
    accentContainer = Color(0xFFEECCCC)
)

val LocalColorProvider = staticCompositionLocalOf<TrackerColors> { error("No default implementation") }
