package models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class Project(val id: Int, val name: String)

object Projects : IntIdTable() {
    val name = varchar("color", 256)
}

private fun fromRow(row: ResultRow) =
        Project(
                row[Projects.id].value,
                row[Projects.name]
        )

class ProjectsDao(private val tasksDao: TasksDao) {

    fun create(name: String): Int = transaction {
        Projects.insertAndGetId {
            it[Projects.name] = name
        }.value
    }

    fun findById(id: Int): Project = transaction {
        val row = Projects.select { Projects.id.eq(id) }.single()
        fromRow(row)
    }

    fun all() = transaction {
        val results = Projects.selectAll().toList()
        results.map {
            fromRow(it)
        }
    }

    fun addTask(color: String, projectId: Int) = transaction {
        val project = findById(projectId)
        tasksDao.insert(Task(0, color, TaskStatus.ready, project.id))
    }

}