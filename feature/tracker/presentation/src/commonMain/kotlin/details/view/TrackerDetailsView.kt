package details.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import components.ClickableTag
import components.DefaultSingleLineTextField
import components.DropDownMenu
import components.DropDownMenuItem
import components.FullWidthSingleLineTextField
import model.TrackerActivity
import model.TrackerProject
import model.TrackerTaskHint
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.typography
import utils.MaskVisualTransformation

@Composable
fun TrackerDetailsView(
    project: String,
    activity: String?,
    task: String,
    description: String,
    start: String,
    end: String?,
    duration: String,
    projectsSuggestions: List<TrackerProject>,
    taskSuggestions: List<TrackerTaskHint>,
    descriptionSuggestions: List<String>,
    activitiesList: List<TrackerActivity>,
    errorText: String? = null,
    onProjectChange: (String) -> Unit,
    onProjectSelect: (Int) -> Unit,
    onTaskChange: (String) -> Unit,
    onTaskSelect: (TrackerTaskHint) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDescriptionSelect: (String) -> Unit,
    onActivityClick: () -> Unit,
    onActivitySelect: (Int) -> Unit,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = colors.primaryContainerBackground)
            .padding(all = dimens.medium)
            .fillMaxSize()
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onCloseClick) {
                Icon(imageVector = Icons.Default.Close, tint = colors.onPrimaryContainerText, contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onCreateClick) {
                Text("Готово", style = typography.headerNormal, color = colors.accent)
            }
        }
        Spacer(modifier = Modifier.height(dimens.medium))
        Text(
            text = duration,
            style = typography.headerLarge,
            color = colors.onPrimaryContainerText
        )
        Spacer(modifier = Modifier.height(dimens.medium))
        TextFieldWithSuggestions(
            value = project,
            suggestions = projectsSuggestions,
            textStyle = typography.headerNormal,
            placeholder = "Ключ проекта",
            formatSuggestion = { key },
            onValueChange = onProjectChange,
            onSelect = { project -> onProjectSelect(project.id) }
        )
        Spacer(modifier = Modifier.height(dimens.medium))
        TextFieldWithSuggestions(
            value = task,
            suggestions = taskSuggestions,
            placeholder = "Ключ задачи",
            formatSuggestion = { "$name $summary" },
            onValueChange = onTaskChange,
            onSelect = onTaskSelect
        )
        Spacer(modifier = Modifier.height(dimens.medium))
        TextFieldWithSuggestions(
            value = description,
            suggestions = descriptionSuggestions,
            placeholder = "Описание",
            onValueChange = onDescriptionChange,
            onSelect = onDescriptionSelect
        )
        Spacer(modifier = Modifier.height(dimens.medium))
        ActivitySelector(
            activity = activity,
            activitiesList = activitiesList,
            onClick = onActivityClick,
            onSelect = onActivitySelect
        )
        // fix exit animation
        AnimatedVisibility(visible = !errorText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(dimens.medium))
            Text(
                text = errorText ?: "", // не нравится)
                style = typography.headerNormal,
                color = colors.accent,
            )
        }

        Spacer(modifier = Modifier.height(dimens.medium))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefaultSingleLineTextField(
                modifier = Modifier.weight(2f),
                value = start,
                visualTransformation = MaskVisualTransformation("##:##"),
                onValueChange = onStartTimeChange
            )
            Spacer(modifier = Modifier.weight(if (end != null) 1f else 3f))
            if (end != null) {
                DefaultSingleLineTextField(
                    modifier = Modifier.weight(2f),
                    value = end,
                    visualTransformation = MaskVisualTransformation("##:##"),
                    onValueChange = onEndTimeChange
                )
            }
        }
    }
}

@Composable
fun <T> TextFieldWithSuggestions(
    modifier: Modifier = Modifier,
    value: String,
    suggestions: List<T>,
    textStyle: TextStyle = typography.bodyNormal,
    placeholder: String? = null,
    formatSuggestion: T.() -> String = { toString() },
    onValueChange: (String) -> Unit,
    onSelect: (T) -> Unit
) {
    val isVisible = remember { mutableStateOf(false) }
    val textFieldSize = remember { mutableStateOf(Size.Zero) }
    Column(modifier = modifier) {
        FullWidthSingleLineTextField(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to the DropDown the same width
                    textFieldSize.value = coordinates.size.toSize()
                }
                .onFocusChanged {
                    if (!it.isFocused) {
                        isVisible.value = false
                    }
                }
            ,
            value = value,
            textStyle = textStyle,
            placeholder = placeholder,
            onValueChange = {
                isVisible.value = true
                onValueChange(it)
            }
        )
        Spacer(modifier = Modifier.height(dimens.default))
        DropDownMenu(
            modifier = Modifier.width(
                with(LocalDensity.current) {
                    textFieldSize.value.width.toDp()
                }
            ),
            expanded = isVisible.value,
            offset = IntOffset(x = 0, y = textFieldSize.value.height.toInt() + 30), // use dp
            onDismissRequest = {
                isVisible.value = false
            }
        ) {
            items(items = suggestions) { suggestion ->
                DropDownMenuItem(
                    text = suggestion.formatSuggestion(),
                    onClick = {
                        onSelect(suggestion)
                        isVisible.value = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ActivitySelector(
    activity: String?,
    activitiesList: List<TrackerActivity>,
    onClick: () -> Unit,
    onSelect: (Int) -> Unit
) {
    val activityPopupExpanded = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        ClickableTag(
            text = activity ?: "Не выбрано",
            onClick = {
                activityPopupExpanded.value = true
                onClick()
            }
        )
        Spacer(modifier = Modifier.height(dimens.default))
        DropDownMenu(
            modifier = Modifier.width(240.dp),
            expanded = activityPopupExpanded.value,
            onDismissRequest = { activityPopupExpanded.value = false }
        ) {
            items(items = activitiesList) { activity ->
                DropDownMenuItem(
                    text = activity.name,
                    onClick = {
                        activityPopupExpanded.value = false
                        onSelect(activity.id)
                    }
                )
            }
        }
    }
}
