package list.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import model.TrackerDateGroup
import model.TrackerListItem
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes
import theme.Theme.typography

@Composable
fun TrackerRecordsView(
    modifier: Modifier = Modifier,
    recordsItems: List<TrackerDateGroup>,
    onRecordClick: (String) -> Unit,
    onTaskGroupClick: (TrackerListItem.TaskGroup) -> Unit
) {
    RecordsList(
        modifier = modifier,
        recordsItems = recordsItems,
        onRecordClick = onRecordClick,
        onTaskGroupClick = onTaskGroupClick
    )
}

@Composable
private fun RecordsList(
    modifier: Modifier = Modifier,
    recordsItems: List<TrackerDateGroup>,
    onRecordClick: (String) -> Unit,
    onTaskGroupClick: (TrackerListItem.TaskGroup) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = colors.primaryBackground)
    ) {
        items(items = recordsItems, key = TrackerDateGroup::date) { item ->
            Spacer(modifier = Modifier.height(dimens.medium))
            DateGroupCard(
                dateGroup = item,
                onRecordClick = onRecordClick,
                onTaskGroupClick = onTaskGroupClick
            )
        }
        item {
            Spacer(modifier = Modifier.height(dimens.medium))
        }
    }
}

@Composable
private fun DateGroupCard(
    dateGroup: TrackerDateGroup,
    onRecordClick: (String) -> Unit,
    onTaskGroupClick: (TrackerListItem.TaskGroup) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = dimens.normal)
            .background(color = colors.primaryContainerBackground, shape = shapes.roundedDefault)
            .padding(all = dimens.medium)
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DateHeader(date = dateGroup.date, totalTime = dateGroup.totalTime)
            Spacer(modifier = Modifier.height(dimens.medium))
            dateGroup.items.forEach { item ->
                when (item) {
                    is TrackerListItem.TaskGroup -> TrackerTaskGroup(
                        taskGroup = item,
                        onClick = onTaskGroupClick,
                        onRecordClick = onRecordClick
                    )

                    is TrackerListItem.Record -> TrackerRecord(
                        record = item,
                        onClick = onRecordClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DateHeader(date: String, totalTime: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        HeaderText(text = date)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = totalTime,
            style = typography.headerNormal,
            color = colors.accent
        )
    }
}

@Composable
private fun TrackerTaskGroup(
    taskGroup: TrackerListItem.TaskGroup,
    onClick: (TrackerListItem.TaskGroup) -> Unit,
    onRecordClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TaskGroupHeader(
            name = taskGroup.name,
            totalTime = taskGroup.totalTime,
            taskGroupExpanded = taskGroup.isExpanded,
            onClick = { onClick(taskGroup) }
        )
        AnimatedVisibility(visible = taskGroup.isExpanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                taskGroup.records.forEach { record ->
                    TaskGroupRecord(record = record, onClick = onRecordClick)
                }
            }
        }
    }
}

@Composable
private fun TaskGroupHeader(
    name: String,
    totalTime: String,
    taskGroupExpanded: Boolean,
    onClick: () -> Unit
) {
    val iconRotationDegrees by animateFloatAsState(targetValue = if (taskGroupExpanded) 90f else 0f)
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.medium)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.rotate(iconRotationDegrees),
            imageVector = Icons.Default.KeyboardArrowRight,
            tint = colors.onPrimaryContainerText,
            contentDescription = null
        )
        HeaderText(modifier = Modifier.weight(1f), text = name.ifEmpty { "Задача не выбрана" })
        Spacer(modifier = Modifier.width(dimens.normal))
        HeaderText(text = totalTime)
    }
}

@Composable
private fun TaskGroupRecord(
    record: TrackerListItem.Record,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.default)
            .padding(start = dimens.extraLarge)
            .clickable { onClick(record.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(dimens.normal))
        Text(
            modifier = Modifier.weight(1f),
            text = record.description,
            style = typography.bodyNormal,
            color = colors.onPrimaryContainerText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(dimens.normal))
        Text(
            text = record.duration,
            style = typography.headerNormal.copy(fontWeight = FontWeight.Light),
            color = colors.onPrimaryContainerText
        )
    }
}

@Composable
private fun TrackerRecord(
    record: TrackerListItem.Record,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.default)
            .clickable { onClick(record.id) }
    ) {
        Spacer(modifier = Modifier.height(dimens.normal))
        Column(modifier = Modifier.weight(1f)) {
            HeaderText(text = record.task.ifEmpty { record.project })
            Spacer(modifier = Modifier.height(dimens.default))
            Text(
                text = record.description,
                style = typography.bodyNormal,
                color = colors.onPrimaryContainerText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(dimens.normal))
        Text(
            text = record.duration,
            style = typography.headerNormal.copy(
                fontWeight = FontWeight.Medium,
                color = colors.onPrimaryContainerText
            )
        )
    }
}

@Composable
private fun HeaderText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = typography.headerNormal.copy(color = colors.onPrimaryContainerText)
    )
}
