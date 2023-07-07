package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import model.TrackerRecordDetails
import theme.Theme.colors
import theme.Theme.dimens
import theme.Theme.shapes
import theme.Theme.typography

@Composable
fun TrackerBar(recordDetails: TrackerRecordDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.primaryContainerBackground, shape = shapes.roundedDefault)
            .padding(dimens.default),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Text(
                text = recordDetails.duration ?: "00:00:00",
                style = typography.headerMedium,
                color = colors.primaryContainerText
            )
            Text(
                text = recordDetails.task ?: "",
                style = typography.headerNormal,
                color = colors.primaryContainerText
            )
            Text(
                text = recordDetails.description ?: "",
                style = typography.bodyNormal,
                color = colors.primaryContainerText
            )
        }
        Spacer(modifier = Modifier.width(dimens.large))
        Text(
            text = recordDetails.activity ?: "None",
            style = typography.bodySmall,
            color = colors.accentText
        )
    }
}
