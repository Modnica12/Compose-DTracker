package theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class TrackerTypography(
    val trackerItemHeader: TextStyle,
    val trackerRecordItemDescription: TextStyle
)

internal val defaultTypography = TrackerTypography(
    trackerItemHeader = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    trackerRecordItemDescription = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

internal val LocalTypographyProvider = staticCompositionLocalOf<TrackerTypography> { error("No default implementation") }
