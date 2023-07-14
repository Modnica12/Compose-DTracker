package list.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.details.TrackerRecordDetails
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes
import theme.Theme.typography

@Composable
fun TrackerBottomBar(
    recordDetails: TrackerRecordDetails,
    tracking: Boolean,
    onStartClick: () -> Unit
) {
    Crossfade(targetState = tracking) {
        if (tracking) {
            TrackingContent(recordDetails = recordDetails)
        } else {
            InactionContent(onStartClick = onStartClick)
        }
    }
}

@Composable
private fun TrackingContent(recordDetails: TrackerRecordDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.primaryBackground)
            .padding(all = dimens.normal),
    ) {
        TimeAndActivity(duration = recordDetails.duration, activity = recordDetails.activity)
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
            ActivityTag(activity)
        }
    }
}

@Composable
private fun ActivityTag(activity: String) {
    Box(
        modifier = Modifier
            .background(
                color = colors.accentContainer,
                shape = shapes.roundedDefault
            )
            .padding(all = dimens.default)
    ) {
        Text(
            text = activity,
            style = typography.bodySmall,
            color = colors.accent
        )
    }
}

@Composable
private fun TaskInfo(task: String?, description: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!task.isNullOrEmpty()) {
            Text(
                text = task,
                style = typography.headerNormal,
                color = colors.onPrimaryContainerText
            )
            Spacer(modifier = Modifier.width(dimens.normal))
        }
        Text(
            modifier = Modifier.weight(1f),
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

