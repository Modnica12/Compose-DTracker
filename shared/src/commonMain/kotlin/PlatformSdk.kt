import configuration.PlatformConfiguration
import org.koin.core.context.startKoin

object PlatformSdk {

    fun initialize(platformConfiguration: PlatformConfiguration) {
        startKoin {
            modules(
                platformModule(platformConfiguration = platformConfiguration),
                coreModule(),
                recordsHistoryModule()
            )
        }
    }
}
