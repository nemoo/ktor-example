package models

import org.jetbrains.exposed.sql.*

data class Task(val id: Int,
                val color: String,
                val status: TaskStatus,
                val project: Int)


object Tasks : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val color = varchar("color", 256)
    val status = varchar("status", 256)
    val project = integer("project")
}

enum class TaskStatus {ready,set,go}


class TasksDao(val db: Database) {
    fun createTask(task: Task) = db.transaction {
         Tasks.insert {
            it[id] = task.id
            it[color] = task.color
            it[status] = task.status.toString()
            it[project] = task.project
        } get Tasks.id
    }

    fun findById(id: Int): Task = db.transaction {
        val row = Tasks.select { Tasks.id.eq(id) }.single()
        Task(id, row[Tasks.color], TaskStatus.valueOf(row[Tasks.status]), row[Tasks.project])
    }

    fun all() = db.transaction {
        val results = Tasks.selectAll().toList()
        results.map {
            Task(it[Tasks.id], it[Tasks.color], TaskStatus.valueOf(it[Tasks.status]), it[Tasks.project])
        }
    }

    fun init() {
        db.transaction {
            create(Tasks)
            createTask(Task(1,"fuchsia",TaskStatus.go,3))
            createTask(Task(2,"black",TaskStatus.set,3))
        }
    }
}