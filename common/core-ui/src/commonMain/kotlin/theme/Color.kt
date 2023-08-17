package theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class TrackerColors(
    val primaryBackground: Color,
    val onPrimaryText: Color,
    val primaryContainerBackground: Color,
    val primaryContainerBorder: Color,
    val onPrimaryContainerText: Color,
    val divider: Color,
    val accent: Color,
    val accentContainer: Color,
    val errorColor: Color,
)

internal val defaultPalette = TrackerColors(
    primaryBackground = Color(0xFFF5F4F5),
    onPrimaryText = Color(0xFF080808),
    primaryContainerBackground = Color.White,
    primaryContainerBorder = Color(0xFFDFDFDF),
    onPrimaryContainerText = Color(0xFF0D0D0D),
    divider = Color(0xFFD8D8D8),
    accent = Color(0xFFE31E30),
    accentContainer = Color(0xFFF9E9EA),
    errorColor = Color(0xFF9D0000)
)

internal val darkPalette = TrackerColors(
    primaryBackground = Color(0xFF181818),
    onPrimaryText = Color(0xFFE2E2E2),
    primaryContainerBackground = Color.Black,
    primaryContainerBorder = Color(0xFF2F2F2F),
    onPrimaryContainerText = Color(0xFFF2F2F2),
    divider = Color(0xFF2D2D2D),
    accent = Color(0xFFE31E30),
    accentContainer = Color(0xFF500000),
    errorColor = Color(0xFF9D0000)
)

val LocalColorProvider = staticCompositionLocalOf<TrackerColors> { error("No color provider") }
