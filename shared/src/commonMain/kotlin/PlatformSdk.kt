import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

object PlatformSdk {

    fun initialize() {
        startKoin {
            modules(coreModule(), recordsHistoryModule())
        }
    }
}

/**
 * Useful to get instance from Koin everywhere.
 */
inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}
