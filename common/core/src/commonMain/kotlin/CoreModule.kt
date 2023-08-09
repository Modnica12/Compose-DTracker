import ktor.ktorModule
import org.koin.dsl.module
import settings.settingsModule
import sqldelight.sqlDelightModule

fun coreModule() = module {
    includes(settingsModule())
    includes(ktorModule())
    includes(sqlDelightModule())
}
