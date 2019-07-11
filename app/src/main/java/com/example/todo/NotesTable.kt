package com.example.todo

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class NotesTable {

    data class Notes (
        val id: Int?,
        val title: String,
        val body : String
    )

    companion object {
        val TABLE_NAME = "notes"

        val CMD_CREATE_TABLE = """
           CREATE TABLE $TABLE_NAME (
           id INTEGER PRIMARY KEY AUTOINCREMENT,
           title TEXT,
           body TEXT
           );
        """.trimIndent()

        fun insertTask(db: SQLiteDatabase, note: Notes) {

            val taskRow = ContentValues()
            taskRow.put("title", note.title)
            taskRow.put("body", note.body)

            db.insert(TABLE_NAME, null, taskRow)

        }

        fun deletAll(db: SQLiteDatabase) {
            db.delete(NotesTable.TABLE_NAME, null, null)
        }

        fun deletS(db: SQLiteDatabase, v : Int?): ArrayList<Notes> {
            db.delete(NotesTable.TABLE_NAME, "id = '$v'", null)

            val notes = ArrayList<Notes>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "title", "body"),
                null, null, //where
                null, // group by
                null, //having
                null //order
            )

            cursor.moveToFirst()

            val idCol = cursor.getColumnIndex("id")
            val titleCol = cursor.getColumnIndex("title")
            val bodyCol = cursor.getColumnIndex("body")

            do  {
                val note = Notes(
                    cursor.getInt(idCol),
                    cursor.getString(titleCol),
                    cursor.getString(bodyCol)
                )
                notes.add(note)
            } while (cursor.moveToNext())
            cursor.close()
            return notes
        }

        fun search(db: SQLiteDatabase, v : String): ArrayList<Notes> {

            val notes = ArrayList<Notes>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "title", "body"),
                "title LIKE '%$v%' OR body LIKE '%$v%'", null, //where
                null, // group by
                null, //having
                null //order
            )

            cursor.moveToFirst()

            val idCol = cursor.getColumnIndex("id")
            val titleCol = cursor.getColumnIndex("title")
            val bodyCol = cursor.getColumnIndex("body")

            do  {
                val note = Notes(
                    cursor.getInt(idCol),
                    cursor.getString(titleCol),
                    cursor.getString(bodyCol)
                )
                notes.add(note)
            } while (cursor.moveToNext())
            cursor.close()
            return notes
        }

        fun getAllTasks(db: SQLiteDatabase): ArrayList<Notes> {

            val notes = ArrayList<Notes>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "title", "body"),
                null, null, //where
                null, // group by
                null, //having
                null //order
            )

            cursor.moveToFirst()

            val idCol = cursor.getColumnIndex("id")
            val titleCol = cursor.getColumnIndex("title")
            val bodyCol = cursor.getColumnIndex("body")

            do  {
                val note = Notes(
                    cursor.getInt(idCol),
                    cursor.getString(titleCol),
                    cursor.getString(bodyCol)
                )
                notes.add(note)
            } while (cursor.moveToNext())
            cursor.close()
            return notes
        }
    }
}