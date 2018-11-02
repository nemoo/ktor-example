package models

data class Task(val id: Long,
                val color: String,
                val status: TaskStatus,
                val project: Long)


enum class TaskStatus {ready,set,go}

fun createTask() = Task(1,"blue", TaskStatus.ready,3)

class TaskRepo(){
    val tasks = listOf<Task>(
            Task(1,"blue", TaskStatus.ready,3)
            ,Task(2,"green", TaskStatus.set,5)
    )
    fun findById(id: Long): Task? = tasks.find { it.id == id }

    fun findByColor(color: String): Task? = tasks.find { it.color == color }

    fun findByProjectId(projectId: Long): List<Task> = tasks.filter { it.project == projectId }

    fun all() = tasks
}