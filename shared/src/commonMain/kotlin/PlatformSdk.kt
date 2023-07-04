import org.koin.core.context.startKoin

object PlatformSdk {

    fun initialize() {
        startKoin {
            modules(coreModule())
        }
    }
}
