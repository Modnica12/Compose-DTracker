package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import theme.Theme
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.typography

@Composable
fun DropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    offset: IntOffset = IntOffset.Zero,
    onDismissRequest: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    PopUp(expanded = expanded, offset = offset, onDismissRequest = onDismissRequest) {
        LazyColumn(
            modifier = modifier
                .height(120.dp)
                .background(
                    color = Theme.colors.primaryBackground,
                    shape = Theme.shapes.roundedDefault
                ),
        ) {
            item {
                Spacer(modifier = Modifier.height(dimens.default))
            }
            content()
            item {
                Spacer(modifier = Modifier.height(dimens.default))
            }
        }
    }
}

@Composable
fun DropDownMenuItem(
    text: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = rememberRipple(color = colors.primaryContainerBackground)
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = interactionSource, indication = indication) { onClick() }
            .padding(vertical = dimens.default, horizontal = dimens.normal),
        text = text,
        style = typography.bodyNormal,
        color = colors.onPrimaryContainerText
    )
}
