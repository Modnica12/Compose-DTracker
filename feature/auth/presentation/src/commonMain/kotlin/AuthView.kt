
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import components.DefaultTextButton
import components.FullWidthSingleLineTextField
import components.PasswordTextField
import theme.Theme.colors
import theme.Theme.dimens

@Composable
fun AuthView(
    userName: String,
    password: String,
    passwordVisible: Boolean,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimens.medium)
            .background(color = colors.primaryBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FullWidthSingleLineTextField(
            value = userName,
            placeholder = "username",
            onValueChange = onUserNameChange
        )
        Spacer(modifier = Modifier.height(dimens.medium))
        PasswordTextField(
            password = password,
            passwordVisible = passwordVisible,
            onVisibilityClick = onPasswordVisibilityClick,
            onPasswordChange = onPasswordChange
        )
        Spacer(modifier = Modifier.height(dimens.extraLarge))
        DefaultTextButton(modifier = Modifier.fillMaxWidth(), text = "Login", onClick = onLoginClick)
    }
}
