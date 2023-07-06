package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Record
import model.TaskGroup
import model.TrackerDateGroup
import theme.Theme.colors
import theme.Theme.typography

@Composable
fun TrackerRecordsView(recordsItems: List<TrackerDateGroup>) {
    RecordsList(recordsItems = recordsItems)
}

@Composable
private fun RecordsList(recordsItems: List<TrackerDateGroup>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.primaryBackground)
    ) {
        items(items = recordsItems, key = TrackerDateGroup::date) { item ->
            Spacer(modifier = Modifier.height(16.dp))
            DateGroupCard(dateGroup = item)
        }
    }
}

@Composable
private fun DateGroupCard(dateGroup: TrackerDateGroup) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(color = colors.primaryContainerBackground, shape = RoundedCornerShape(4.dp))
            .padding(all = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DateHeader(date = dateGroup.date, totalTime = dateGroup.totalTime)
            Spacer(modifier = Modifier.height(16.dp))
            dateGroup.items.forEach { item ->
                when (item) {
                    is TaskGroup -> TrackerTaskGroup(taskGroup = item)
                    is Record -> TrackerRecord(record = item)
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
            style = typography.trackerItemHeader.copy(color = colors.accentText)
        )
    }
}

@Composable
private fun TrackerTaskGroup(taskGroup: TaskGroup) {
    val expanded = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        TaskGroupHeader(
            name = taskGroup.name,
            totalTime = taskGroup.totalTime,
            taskGroupExpanded = expanded.value,
            onClick = { expanded.value = !expanded.value }
        )
        AnimatedVisibility(visible = expanded.value) {
            Column(modifier = Modifier.fillMaxWidth()) {
                taskGroup.records.forEach { record ->
                    TaskGroupRecord(record = record)
                }
            }
        }
    }
}

@Composable
private fun TaskGroupHeader(name: String, totalTime: String, taskGroupExpanded: Boolean, onClick: () -> Unit) {
    val iconRotationDegrees by animateFloatAsState(targetValue = if (taskGroupExpanded) 90f else 0f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.rotate(iconRotationDegrees),
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null
        )
        HeaderText(modifier = Modifier.weight(1f), text = name.ifEmpty { "Задача не выбрана" })
        Spacer(modifier = Modifier.width(12.dp))
        HeaderText(text = totalTime)
    }
}

@Composable
private fun TaskGroupRecord(record: Record) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = record.description,
            style = typography.trackerRecordItemDescription,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = record.duration,
            style = typography.trackerItemHeader.copy(
                fontWeight = FontWeight.Light,
                color = colors.primaryContainerText
            )
        )
    }
}

@Composable
private fun TrackerRecord(record: Record) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            HeaderText(text = record.task.ifEmpty { record.project })
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = record.description,
                style = typography.trackerRecordItemDescription,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = record.duration,
            style = typography.trackerItemHeader.copy(
                fontWeight = FontWeight.Medium,
                color = colors.primaryContainerText
            )
        )
    }
}

@Composable
private fun HeaderText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = typography.trackerItemHeader.copy(color = colors.primaryContainerText)
    )
}
