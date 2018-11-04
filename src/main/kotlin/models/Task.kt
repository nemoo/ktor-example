package models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class Task(val id: Int,
                val color: String,
                val status: TaskStatus,
                val project: Int)


object Tasks : IntIdTable() {
    val color = varchar("color", 256)
    val status = varchar("status", 256)
    val project = integer("project")
}

private fun fromRow(row: ResultRow) =
    Task(
            row[Tasks.id].value,
            row[Tasks.color],
            TaskStatus.valueOf(row[Tasks.status]),
            row[Tasks.project]
    )

enum class TaskStatus {ready,set,go}

class TasksDao(private val db: Database) {
    fun insert(task: Task): Int = transaction(db){
         Tasks.insertAndGetId {
            it[color] = task.color
            it[status] = task.status.toString()
            it[project] = task.project
        }.value
    }

    fun findById(id: Int): Task = transaction(db) {
        val row = Tasks.select { Tasks.id.eq(id) }.single()
        fromRow(row)
    }

    fun all() = transaction(db) {
        val results = Tasks.selectAll().toList()
        results.map {
            fromRow(it)
        }
    }
}