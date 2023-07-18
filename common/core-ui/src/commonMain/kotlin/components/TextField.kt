package components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import theme.Theme.colors
import theme.Theme.shapes
import theme.Theme.typography

@Composable
fun FullWidthTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    textStyle: TextStyle = typography.bodyNormal,
    placeholder: String? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        textStyle = textStyle,
        shape = shapes.roundedDefault,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = colors.onPrimaryContainerText,
            unfocusedBorderColor = colors.primaryContainerBorder,
            focusedBorderColor = colors.primaryContainerBorder
        ),
        placeholder = { placeholder?.let { Text(text = it) } },
        onValueChange = onValueChange
    )
}
