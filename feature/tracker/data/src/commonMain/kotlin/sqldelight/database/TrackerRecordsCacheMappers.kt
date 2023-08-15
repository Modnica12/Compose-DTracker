package sqldelight.database

import database.TrackerRecordCache
import datetime.parseToDateTime
import model.TrackerRecord
import model.cache.toCache
import model.cache.toDomain
import model.response.TrackerRecordRemote

internal fun TrackerRecordCache.toDomain(): TrackerRecord =
    TrackerRecord(
        id = id,
        project = project?.toDomain(),
        activity = activity?.toDomain(),
        task = task?.toDomain(),
        start = start.parseToDateTime(),
        duration = duration.toInt(),
        description = description
    )

internal fun TrackerRecord.toCache(): TrackerRecordCache =
    TrackerRecordCache(
        id = id,
        project = project?.toCache(),
        activity = activity?.toCache(),
        task = task?.toCache(),
        start = start.toString(),
        duration = duration.toLong(),
        description = description
    )

internal fun TrackerRecordRemote.toCache(): TrackerRecordCache =
    TrackerRecordCache(
        id = id,
        project = project.toCache(),
        activity = activity?.toCache(),
        task = task.toCache(),
        start = start,
        duration = duration.toLong(),
        description = description
    )
