package theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalColorProvider provides defaultPalette,
        LocalTypographyProvider provides defaultTypography,
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
}
