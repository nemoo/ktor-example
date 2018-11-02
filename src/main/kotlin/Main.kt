import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import models.TaskRepo


fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {
    routing {
        install(ContentNegotiation) { jackson() }
        install(StatusPages)
        install(CallLogging)
        install(Locations)

        taskRoutes()
        projectRoutes()
    }
}

fun Routing.taskRoutes() {
    get("/tasks") {
        val taskRepo = models.TaskRepo()
        val list = taskRepo.all()
        call.respond(list)
    }

    @Location("/tasks/{id}")
    data class GetTasks(val id: Long)
    get<GetTasks> {
        val id: Long = it.id
        val taskRepo = models.TaskRepo()
        val task = taskRepo.findById(id)!!
        call.respond(task)
    }
}

fun Routing.projectRoutes() {
    get("/projects") {
        call.respondText("All Projects")
    }

}