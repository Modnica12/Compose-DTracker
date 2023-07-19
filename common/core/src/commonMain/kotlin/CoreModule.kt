import ktor.ktorModule
import org.koin.dsl.module
import sqldelight.sqlDelightModule

fun coreModule() = module {
    includes(ktorModule())
    includes(sqlDelightModule())
}
