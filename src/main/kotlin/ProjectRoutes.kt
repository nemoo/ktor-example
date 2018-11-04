import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing
import models.ProjectsDao

@KtorExperimentalLocationsAPI
fun Routing.projectRoutes(projectsDao: ProjectsDao) {

    @Location("/projects")
    class GetProjects

    get<GetProjects> {
        val list = projectsDao.all()
        call.respond(list)
    }
    
    @Location("projects/{id}")
    data class GetProject(val id: Int)

    get<GetProject> {
        val project = projectsDao.findById(it.id)
        call.respond(project)
    }

}