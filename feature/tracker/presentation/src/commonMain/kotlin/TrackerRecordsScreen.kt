import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.ViewModel
import com.adeo.kviewmodel.compose.observeAsState
import view.TrackerRecordsView

@Composable
fun TrackerRecordsScreen() {
    ViewModel(factory = { TrackerRecordsViewModel() }) { viewModel ->
        val state by viewModel.viewStates().observeAsState()
        TrackerRecordsView(recordsItems = state.records)
    }
}
