
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import models.ProjectsDao
import models.TasksDao
import models.TestData
import org.jetbrains.exposed.sql.Database

@KtorExperimentalLocationsAPI
fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module = Application::mainModule).start(wait = true)
}

@KtorExperimentalLocationsAPI
fun Application.mainModule() {

    val db: Database = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    val tasksDao = TasksDao()
    val projectsDao = ProjectsDao(tasksDao)
    val testData = TestData(projectsDao)

    testData.init()

    install(ContentNegotiation) { jackson() }
    install(StatusPages)
    install(CallLogging)
    install(Locations)

    routing {
        taskRoutes(tasksDao)
        projectRoutes(projectsDao)
    }
}
