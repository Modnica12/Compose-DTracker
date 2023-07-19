import configuration.PlatformConfiguration
import org.koin.dsl.module

fun platformModule(platformConfiguration: PlatformConfiguration) = module {
    single { platformConfiguration }
}
