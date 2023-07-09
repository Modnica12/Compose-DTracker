package theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class TrackerTypography(
    val headerLarge: TextStyle,
    val headerMedium: TextStyle,
    val headerNormal: TextStyle,
    val bodyNormal: TextStyle,
    val bodySmall: TextStyle
)

internal val defaultTypography = TrackerTypography(
    headerLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp
    ),
    headerMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    headerNormal = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    bodyNormal = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)

internal val LocalTypographyProvider = staticCompositionLocalOf<TrackerTypography> { error("No default implementation") }
