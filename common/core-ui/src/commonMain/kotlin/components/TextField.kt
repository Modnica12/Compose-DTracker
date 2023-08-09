package components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    placeholder: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = TextFieldValue(text = value, selection = TextRange(value.length)),
        enabled = enabled,
        textStyle = textStyle,
        shape = shapes.roundedDefault,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
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
        trailingIcon = trailingIcon,
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    DefaultTextField(
        modifier = modifier,
        value = value,
        enabled = enabled,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    placeholder: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    DefaultTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    DefaultSingleLineTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        onValueChange = onValueChange
    )
}

@Composable
fun PasswordTextField(
    password: String,
    passwordVisible: Boolean,
    placeholder: String = "password",
    onVisibilityClick: () -> Unit,
    onPasswordChange: (String) -> Unit
) {
    FullWidthSingleLineTextField(
        value = password,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        placeholder = placeholder,
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = onVisibilityClick) {
                Icon(imageVector = icon, tint = colors.accent, contentDescription = null)
            }
        },
        onValueChange = onPasswordChange
    )
}
