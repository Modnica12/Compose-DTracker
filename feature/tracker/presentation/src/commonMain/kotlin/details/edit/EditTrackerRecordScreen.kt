package details.edit

import androidx.compose.runtime.Composable
import com.adeo.kviewmodel.odyssey.StoredViewModel
import details.base.TrackerRecordScreen

@Composable
fun EditTrackerRecordScreen(recordId: String) {
    StoredViewModel(factory = { EditTrackerRecordViewModel(recordId) }) { viewModel ->
        TrackerRecordScreen(viewModel = viewModel)
    }
}
