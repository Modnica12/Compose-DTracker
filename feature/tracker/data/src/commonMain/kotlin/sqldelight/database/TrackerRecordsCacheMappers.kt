package sqldelight.database

import database.TrackerRecordCache
import model.TrackerRecord
import model.cache.toCache
import model.cache.toDomain
import utils.parseToDateTime

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
        start = start.toString(), // хм)
        duration = duration.toLong(),
        description = description
    )
