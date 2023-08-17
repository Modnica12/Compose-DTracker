package details.currentRecord

import androidx.compose.runtime.Composable
import com.adeo.kviewmodel.odyssey.StoredViewModel
import details.base.TrackerRecordScreen

@Composable
fun CurrentRecordScreen() {
    StoredViewModel(factory = { CurrentRecordViewModel() }) { viewModel ->
        TrackerRecordScreen(viewModel = viewModel)
    }
}
