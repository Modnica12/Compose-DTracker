package components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
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
        value = TextFieldValue(text = value, selection = TextRange(value.length)),
        enabled = enabled,
        textStyle = textStyle,
        shape = shapes.roundedDefault,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = colors.onPrimaryText,
            backgroundColor = colors.primaryBackground,
            unfocusedBorderColor = colors.primaryContainerBorder,
            focusedBorderColor = colors.primaryContainerBorder
        ),
        placeholder = {
            placeholder?.let {
                Text(text = it, style = typography.bodyNormal, color = colors.onPrimaryText)
            }
        },
        onValueChange = { value -> onValueChange(value.text) }
    )
}
