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
import androidx.compose.ui.text.input.VisualTransformation
import theme.Theme.colors
import theme.Theme.shapes
import theme.Theme.typography

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    textStyle: TextStyle = typography.bodyNormal,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    placeholder: String? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = TextFieldValue(text = value, selection = TextRange(value.length)),
        enabled = enabled,
        textStyle = textStyle,
        shape = shapes.roundedDefault,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = colors.onPrimaryText,
            backgroundColor = colors.primaryBackground,
            unfocusedBorderColor = colors.primaryContainerBorder,
            focusedBorderColor = colors.primaryContainerBorder,
            cursorColor = colors.accent
        ),
        placeholder = {
            placeholder?.let {
                Text(text = it, style = typography.bodyNormal, color = colors.onPrimaryText)
            }
        },
        onValueChange = { value -> onValueChange(value.text) }
    )
}

@Composable
fun DefaultSingleLineTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    textStyle: TextStyle = typography.bodyNormal,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholder: String? = null,
    onValueChange: (String) -> Unit
) {
    DefaultTextField(
        modifier = modifier,
        value = value,
        enabled = enabled,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        singleLine = true,
        placeholder = placeholder,
        onValueChange = onValueChange
    )
}

@Composable
fun FullWidthTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    textStyle: TextStyle = typography.bodyNormal,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    placeholder: String? = null,
    onValueChange: (String) -> Unit
) {
    DefaultTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        placeholder = placeholder,
        onValueChange = onValueChange
    )
}

@Composable
fun FullWidthSingleLineTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean = true,
    textStyle: TextStyle = typography.bodyNormal,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholder: String? = null,
    onValueChange: (String) -> Unit
) {
    DefaultSingleLineTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        placeholder = placeholder,
        onValueChange = onValueChange
    )
}
