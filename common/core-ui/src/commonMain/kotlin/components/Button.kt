package components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import theme.Theme.colors
import theme.Theme.dimens

@Composable
fun DefaultTextButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(backgroundColor = colors.accent, contentColor = colors.primaryBackground),
        contentPadding = PaddingValues(vertical = dimens.medium),
        onClick = onClick
    ) {
        Text(text = text)
    }
}
