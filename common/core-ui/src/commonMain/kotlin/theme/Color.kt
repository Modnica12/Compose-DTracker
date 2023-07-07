package theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class TrackerColors(
    val primaryBackground: Color,
    val onPrimaryText: Color,
    val primaryContainerBackground: Color,
    val onPrimaryContainerText: Color,
    val accent: Color,
    val accentBackground: Color,
    val onAccent: Color,
)

internal val defaultPalette = TrackerColors(
    primaryBackground = Color(0xFFE2E2E2),
    onPrimaryText = Color(0xFF080808),
    primaryContainerBackground = Color(0xFFFFFFFF),
    onPrimaryContainerText = Color(0xFF0D0D0D),
    accent = Color(0xFFC00000),
    accentBackground = Color(0xFFC50000),
    onAccent = Color(0xFFF2F2F2),
)

val LocalColorProvider = staticCompositionLocalOf<TrackerColors> { error("No default implementation") }
