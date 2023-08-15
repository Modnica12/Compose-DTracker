package currentRecord

import TrackerRecordsRepository
import datetime.getCurrentDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        get() = job + Dispatchers.Default

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
            repository.updateCurrentRecord { copy(duration = duration) }
            while (true) {
                delay(1000)
                duration = repository.currentRecord.value?.duration ?: duration
                duration += 1
                repository.updateCurrentRecord { copy(duration = duration) }
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
        val currentRecord = currentRecord.value
        (newProjectId ?: currentRecord?.project?.id)?.let { projectId ->
            launch {
                repository.startTracker(
                    projectId = projectId,
                    activityId = newActivityId ?: currentRecord?.activity?.id,
                    task = newTask ?: currentRecord?.task?.name ?: "",
                    description = newDescription ?: currentRecord?.description ?: "",
                    start = newStart ?: currentRecord?.start ?: getCurrentDateTime()
                ).apply {
                    repository.setCurrentRecord(getOrNull())
                }
            }
        }
    }
}
