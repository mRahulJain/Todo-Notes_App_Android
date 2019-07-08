package com.example.todo

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class TasksTable {

    data class Task (
        val id: Int?,
        val task: String,
        var done: Boolean
    )

    companion object {
        val TABLE_NAME = "tasks"

        val CMD_CREATE_TABLE = """
           CREATE TABLE $TABLE_NAME (
           id INTEGER PRIMARY KEY AUTOINCREMENT,
           task TEXT,
           done BOOLEAN
           );
        """.trimIndent()

        fun insertTask(db: SQLiteDatabase, task: Task) {

            val taskRow = ContentValues()
            taskRow.put("task", task.task)
            taskRow.put("done", task.done)

            db.insert(TABLE_NAME, null, taskRow)

        }

        fun updateTask(db: SQLiteDatabase, task: Task) {

            val taskRow = ContentValues()
            taskRow.put("task", task.task)
            taskRow.put("done", task.done)

            db.update(TABLE_NAME, taskRow, "id = ?", arrayOf(task.id.toString()))
        }

        fun deletTask(db: SQLiteDatabase) {
            db.delete(TABLE_NAME, "done=1", null)
        }

        fun deletSTask(db: SQLiteDatabase, v : Int?) : ArrayList<Task> {
            db.delete(TABLE_NAME, "id = '$v'", null)

            val tasks = ArrayList<Task>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "task", "done"),
                null, null, //where
                null, // group by
                null, //having
                null //order
            )

            cursor.moveToFirst()

            val idCol = cursor.getColumnIndex("id")
            val taskCol = cursor.getColumnIndex("task")
            val doneCol = cursor.getColumnIndex("done")

            while (cursor.moveToNext()) {
                val task = Task(
                    cursor.getInt(idCol),
                    cursor.getString(taskCol),
                    cursor.getInt(doneCol) == 1
                )
                tasks.add(task)
            }
            cursor.close()
            return tasks
        }

        fun sortTask(db : SQLiteDatabase) : ArrayList<Task> {
            val tasks = ArrayList<Task>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "task", "done"),
                 null, null, //where
                null, // group by
                null, //having
                "done ASC" //order
            )

            cursor.moveToFirst()

            val idCol = cursor.getColumnIndex("id")
            val taskCol = cursor.getColumnIndex("task")
            val doneCol = cursor.getColumnIndex("done")

            while (cursor.moveToNext()) {
                val task = Task(
                    cursor.getInt(idCol),
                    cursor.getString(taskCol),
                    cursor.getInt(doneCol) == 1
                )
                tasks.add(task)
            }
            cursor.close()
            return tasks
        }

        fun search(db: SQLiteDatabase, v : String) : ArrayList<Task> {
            val tasks = ArrayList<Task>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "task", "done"),
                "task LIKE '%$v%' AND done=0", null, //where
                null, // group by
                null, //having
                null //order
            )

            cursor.moveToFirst()

            val idCol = cursor.getColumnIndex("id")
            val taskCol = cursor.getColumnIndex("task")
            val doneCol = cursor.getColumnIndex("done")

            while (cursor.moveToNext()) {
                val task = Task(
                    cursor.getInt(idCol),
                    cursor.getString(taskCol),
                    cursor.getInt(doneCol) == 1
                )
                tasks.add(task)
            }
            cursor.close()
            return tasks
        }

        fun getAllTasks(db: SQLiteDatabase): ArrayList<Task> {

            val tasks = ArrayList<Task>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "task", "done"),
                null, null, //where
                null, // group by
                null, //having
                null //order
            )

            cursor.moveToFirst()

            val idCol = cursor.getColumnIndex("id")
            val taskCol = cursor.getColumnIndex("task")
            val doneCol = cursor.getColumnIndex("done")

            while (cursor.moveToNext()) {
                val task = Task(
                    cursor.getInt(idCol),
                    cursor.getString(taskCol),
                    cursor.getInt(doneCol) == 1
                )
                tasks.add(task)
            }
            cursor.close()
            return tasks
        }

    }
}