import ktor.ktorModule
import org.koin.dsl.module

fun coreModule() = module {
    includes(ktorModule())
}
