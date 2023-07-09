package theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape

data class TrackerShapes(
    val roundedDefault: Shape,
    val roundedSmall: Shape
)

val defaultShapes = TrackerShapes(
    roundedDefault = RoundedCornerShape(defaultDimens.default),
    roundedSmall = RoundedCornerShape(defaultDimens.small)
)

val LocalShapeProvider = staticCompositionLocalOf<TrackerShapes> { error("No default implementation") }
