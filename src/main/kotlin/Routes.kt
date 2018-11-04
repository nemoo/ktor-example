import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing
import models.ProjectsDao
import models.TasksDao

fun Routing.taskRoutes(tasksDao: TasksDao) {
    @Location("/tasks")
    class GetTasks

    get<GetTasks> {
        val list = tasksDao.all()
        call.respond(list)
    }

    @Location("/tasks/{id}")
    data class GetTask(val id: Int)

    get<GetTask> {
        val task = tasksDao.findById(it.id)
        call.respond(task)
    }
}

fun Routing.projectRoutes(projectsDao: ProjectsDao) {
    @Location("projects/{id}")
    data class GetProject(val id: Int)

    get<GetProject> {
        val project = projectsDao.findById(it.id)
        call.respond(project)
    }

}