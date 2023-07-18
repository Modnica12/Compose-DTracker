package details.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import components.ClickableTag
import components.FullWidthTextField
import components.PopUp
import model.TrackerActivity
import model.TrackerProject
import model.TrackerTaskHint
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes
import theme.Theme.typography

@Composable
fun TrackerDetailsView(
    project: String,
    activity: String?,
    task: String,
    description: String,
    start: String,
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
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.primaryContainerBackground)
            .padding(all = dimens.medium)
    ) {
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
        AnimatedVisibility(visible = !errorText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(dimens.medium))
            Text(
                text = errorText ?: "", // не нравится)
                style = typography.headerNormal,
                color = colors.accent,
            )
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
    val isFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier) {
        FullWidthTextField(
            modifier = Modifier.onFocusChanged { isFocused.value = it.isFocused },
            value = value,
            textStyle = textStyle,
            placeholder = placeholder,
            onValueChange = onValueChange
        )
        PopUp(
            expanded = isFocused.value,
            onDismissRequest = {
                isFocused.value = false
                focusManager.clearFocus()
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(all = dimens.medium)
                    .background(color = colors.primaryBackground, shape = shapes.roundedDefault)
                    .padding(all = dimens.normal),
            ) {
                items(items = suggestions) { suggestion ->
                    Text(
                        modifier = Modifier.clickable {
                            onSelect(suggestion)
                            isFocused.value = false
                            focusManager.clearFocus()
                        },
                        text = suggestion.formatSuggestion()
                    )
                    Spacer(modifier = Modifier.height(dimens.default))
                }
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
    ClickableTag(
        text = activity ?: "Не выбрано",
        onClick = {
            activityPopupExpanded.value = true
            onClick()
        }
    )
    PopUp(
        expanded = activityPopupExpanded.value,
        onDismissRequest = { activityPopupExpanded.value = false }
    ) {
        LazyColumn(
            modifier = Modifier
                .width(240.dp)
                .height(120.dp)
                .padding(top = dimens.medium)
                .background(color = colors.primaryBackground, shape = shapes.roundedDefault)
                .padding(all = dimens.normal),
        ) {
            items(
                items = activitiesList
            ) {activity ->
                Text(
                    modifier = Modifier.clickable {
                        activityPopupExpanded.value = false
                        onSelect(activity.id)
                    },
                    text = activity.name
                )
                Spacer(modifier = Modifier.height(dimens.default))
            }
        }
    }
}
