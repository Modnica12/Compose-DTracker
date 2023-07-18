package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes
import theme.Theme.typography

@Composable
fun Tag(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .background(
                color = colors.accentContainer,
                shape = shapes.roundedDefault
            )
            .padding(all = dimens.default)
    ) {
        Text(
            text = text,
            style = typography.bodySmall,
            color = colors.accent
        )
    }
}

@Composable
fun ClickableTag(
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = rememberRipple(color = colors.accent)
    Tag(
        modifier = modifier
            .clip(shape = shapes.roundedDefault)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enable,
                onClick = onClick
            ),
        text = text
    )
}
