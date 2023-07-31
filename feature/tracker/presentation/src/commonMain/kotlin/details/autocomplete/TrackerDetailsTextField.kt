package details.autocomplete

internal sealed class TrackerDetailsTextField(open val text: String) {
    data class Project(override val text: String): TrackerDetailsTextField(text)

    data class Task(override val text: String): TrackerDetailsTextField(text)

    data class Description(override val text: String): TrackerDetailsTextField(text)
}
