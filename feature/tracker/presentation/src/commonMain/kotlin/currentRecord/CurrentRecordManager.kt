package currentRecord

import TrackerRecordsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlin.coroutines.CoroutineContext

class CurrentRecordManager(
    private val repository: TrackerRecordsRepository
) : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private var timerJob: Job? = null

    val currentRecord = repository.currentRecord

    init {
        launch {
            repository.getCurrentRecord()
            startTimer()
        }
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = launch {
            var duration = repository.currentRecord.value?.duration ?: 0
            while (true) {
                repository.updateCurrentRecord { copy(duration = duration) }
                duration = repository.currentRecord.value?.duration ?: duration
                duration += 1
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        launch {
            repository.stopTracker()
        }
    }

    fun updateProject(id: Int) {
        updateRecord(newProjectId = id)
    }

    fun updateTask(task: String, description: String? = null) {
        updateRecord(newTask = task)
    }

    fun updateDescription(description: String) {
        updateRecord(newDescription = description)
    }

    fun updateActivity(id: Int) {
        updateRecord(newActivityId = id)
    }

    fun updateStart(start: LocalDateTime) {
        updateRecord(newStart = start)
    }

    fun updateRecord(
        projectId: Int,
        activityId: Int?,
        task: String,
        description: String,
        start: LocalDateTime
    ) {
        updateRecord(
            newProjectId = projectId,
            newActivityId = activityId,
            newTask = task,
            newDescription = description,
            newStart = start
        )
    }

    private fun updateRecord(
        newProjectId: Int? = null,
        newActivityId: Int? = null,
        newTask: String? = null,
        newDescription: String? = null,
        newStart: LocalDateTime? = null,
    ) {
        repository.currentRecord.value?.apply {
            (newProjectId ?: project?.id)?.let { projectId ->
                launch {
                    repository.startTracker(
                        projectId = projectId,
                        activityId = newActivityId ?: activity?.id,
                        task = newTask ?: task?.name ?: "",
                        description = newDescription ?: description,
                        start = newStart ?: start
                    )
                }
            }
        }
    }
}
