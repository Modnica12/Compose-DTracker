package theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalColorProvider provides defaultPalette,
        LocalTypographyProvider provides defaultTypography,
        LocalShapeProvider provides defaultShapes,
        LocalDimenProvider provides defaultDimens,
        content = content
    )
}

object Theme {
    val colors: TrackerColors
        @Composable
        get() = LocalColorProvider.current

    val typography: TrackerTypography
        @Composable
        get() = LocalTypographyProvider.current

    val shapes: TrackerShapes
        @Composable
        get() = LocalShapeProvider.current

    val dimens: TrackerDimens
        @Composable
        get() = LocalDimenProvider.current
}
