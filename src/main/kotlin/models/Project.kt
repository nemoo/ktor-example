package models

import org.jetbrains.exposed.sql.*

data class Project(val id: Int, val name: String)

object Projects : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("color", 256)
}

class ProjectsDao(val db: Database = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")) {
    fun createProject(project: Project) = db.transaction {
        Projects.insert {
            it[id] = project.id
            it[name] = project.name
        } get Projects.id
    }

    fun findById(id: Int): Project = db.transaction {
        val row = Projects.select { Projects.id.eq(id) }.single()
        Project(row[Projects.id], row[Projects.name])
    }

    fun all() = db.transaction {
        val results = Projects.selectAll().toList()
        results.map {
            Project(it[Projects.id], it[Projects.name])
        }
    }

    fun init() {
        db.transaction {
            create(Projects)
            createProject(Project(1,"a"))
            createProject(Project(2,"b"))
        }
    }
}