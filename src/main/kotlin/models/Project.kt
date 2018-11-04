package models

import org.jetbrains.exposed.sql.*

data class Project(val id: Int, val name: String)

object Projects : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("color", 256)
}

private fun fromRow(row: ResultRow) =
        Project(
                row[Projects.id],
                row[Projects.name]
        )

class ProjectsDao(val db: Database) {
    fun createProject(project: Project) = db.transaction {
        Projects.insert {
            it[id] = project.id
            it[name] = project.name
        } get Projects.id
    }

    fun findById(id: Int): Project = db.transaction {
        val row = Projects.select { Projects.id.eq(id) }.single()
        fromRow(row)
    }

    fun all() = db.transaction {
        val results = Projects.selectAll().toList()
        results.map {
            fromRow(it)
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