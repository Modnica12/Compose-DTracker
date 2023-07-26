package list.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.Tag
import model.details.TrackerRecordDetails
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes
import theme.Theme.typography

@Composable
fun TrackerBottomBar(
    recordDetails: TrackerRecordDetails,
    tracking: Boolean,
    onClick: () -> Unit,
    onStartClick: () -> Unit
) {
    if (tracking) {
        TrackingContent(recordDetails = recordDetails, onClick = onClick)
    } else {
        InactionContent(onStartClick = onStartClick)
    }
}

@Composable
private fun TrackingContent(onClick: () -> Unit, recordDetails: TrackerRecordDetails) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .fillMaxWidth()
            .background(color = colors.primaryBackground)
            .padding(all = dimens.normal),
    ) {
        TimeAndActivity(duration = recordDetails.duration, activity = recordDetails.activity?.name)
        Spacer(Modifier.height(dimens.normal))
        TaskInfo(task = recordDetails.task, description = recordDetails.description)
    }
}

@Composable
private fun TimeAndActivity(duration: String?, activity: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = duration ?: "00:00:00",
            style = typography.headerLarge,
            color = colors.onPrimaryContainerText
        )
        Spacer(modifier = Modifier.weight(1f))
        if (!activity.isNullOrEmpty()) {
            Tag(text = activity)
        }
    }
}

@Composable
private fun TaskInfo(task: String?, description: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (!task.isNullOrEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = task,
                style = typography.headerNormal,
                color = colors.onPrimaryContainerText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(dimens.default))
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = description ?: "",
            style = typography.bodyNormal,
            color = colors.onPrimaryContainerText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun InactionContent(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.primaryBackground)
            .padding(all = dimens.medium)
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterStart),
            shape = shapes.roundedDefault,
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = colors.primaryContainerBackground),
            border = BorderStroke(width = 1.dp, color = colors.primaryContainerBorder),
            contentPadding = PaddingValues(all = dimens.medium),
            onClick = onStartClick,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Я работаю над...",
                textAlign = TextAlign.Start,
                style = typography.bodyNormal,
                color = colors.onPrimaryContainerText
            )
        }
    }
}

