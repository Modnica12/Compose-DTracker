package details.autocomplete

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import model.TrackerProject
import model.TrackerTaskHint
import kotlin.coroutines.CoroutineContext

@OptIn(FlowPreview::class)
internal class AutoCompleteTextChangedHandler(
    initialState: TrackerDetailsTextFieldsState = TrackerDetailsTextFieldsState(),
    private val onFieldRequest: (TrackerDetailsTextField) -> Unit,
    private val requestProjectSuggestions: suspend (String) -> List<TrackerProject>,
    private val requestTaskSuggestion: suspend (String) -> List<TrackerTaskHint>,
    private val requestDescriptionSuggestion: suspend (String) -> List<String>,
    private val onProjectSelected: suspend (TrackerProject) -> Unit,
    private val onTaskSelected: suspend (TrackerTaskHint) -> Unit,
    private val onDescriptionSelected: suspend (String) -> Unit,
) : CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val mutableTextFieldsState: MutableStateFlow<TrackerDetailsTextFieldsState> = MutableStateFlow(initialState)
    val textFieldsState: StateFlow<TrackerDetailsTextFieldsState> = mutableTextFieldsState.asStateFlow()

    private val fieldsFlow: MutableStateFlow<TrackerDetailsTextField?> = MutableStateFlow(null)

    init {
        fieldsFlow
            .debounce(TEXT_FIELD_DEBOUNCE)
            .onEach { textField ->
                if (textField != null) {
                    val trimmedText = textField.text.trim()
                    when(textField) {
                        is TrackerDetailsTextField.Project -> {
                            val suggestions = requestProjectSuggestions(trimmedText)
                            updateFieldsState { copy(projectSuggestions = suggestions) }
                        }
                        is TrackerDetailsTextField.Task -> {
                            val suggestions = requestTaskSuggestion(trimmedText)
                            updateFieldsState { copy(taskSuggestions = suggestions) }
                        }
                        is TrackerDetailsTextField.Description -> {
                            val suggestions = requestDescriptionSuggestion(trimmedText)
                            updateFieldsState { copy(descriptionSuggestions = suggestions) }
                        }
                    }
                    onFieldRequest(textField)
                }
            }
            .launchIn(this)
    }

    fun handleTextChange(field: TrackerDetailsTextField) {
        val newText = field.text
        when (field) {
            is TrackerDetailsTextField.Project -> updateFieldsState { copy(projectText = newText) }
            is TrackerDetailsTextField.Task -> updateFieldsState { copy(taskText = newText) }
            is TrackerDetailsTextField.Description -> updateFieldsState { copy(descriptionText = newText) }
        }
        fieldsFlow.value = field
    }

    fun handleSuggestionClick(suggestion: TrackerDetailsTextFieldSuggestion) {
        launch {
            when (suggestion) {
                is TrackerDetailsTextFieldSuggestion.Project -> {
                    updateFieldsState { copy(projectText = suggestion.project.key) }
                    onProjectSelected(suggestion.project)
                }

                is TrackerDetailsTextFieldSuggestion.Task -> {
                    updateFieldsState { copy(taskText = suggestion.task.name) }
                    updateFieldsState { copy(descriptionText = suggestion.task.summary) }
                    onTaskSelected(suggestion.task)
                }

                is TrackerDetailsTextFieldSuggestion.Description -> {
                    updateFieldsState { copy(descriptionText = suggestion.text) }
                    onDescriptionSelected(suggestion.text)
                }
            }
        }
    }

    fun updateFieldsState(transform: TrackerDetailsTextFieldsState.() -> TrackerDetailsTextFieldsState) {
        mutableTextFieldsState.value = textFieldsState.value.transform()
    }

    companion object {
        private const val TEXT_FIELD_DEBOUNCE = 1000L
    }
}
