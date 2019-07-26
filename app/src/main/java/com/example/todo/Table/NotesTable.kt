package com.example.todo

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class NotesTable {

    data class Notes (
        val id: Int?,
        val title: String,
        val body : String,
        var toggle : Int
    )

    companion object {
        val TABLE_NAME = "notes"

        val CMD_CREATE_TABLE = """
           CREATE TABLE $TABLE_NAME (
           id INTEGER PRIMARY KEY AUTOINCREMENT,
           title TEXT,
           body TEXT,
           toggle INTEGER
           );
        """.trimIndent()

        fun insertTask(db: SQLiteDatabase, note: Notes) {

            val taskRow = ContentValues()
            taskRow.put("title", note.title)
            taskRow.put("body", note.body)
            taskRow.put("toggle", note.toggle)

            db.insert(TABLE_NAME, null, taskRow)

        }

        fun deletAll(multiDelet : ArrayList<Notes>,db: SQLiteDatabase) : ArrayList<Notes> {
//            db.delete(TABLE_NAME, null, null)
            for(i in 0..multiDelet.lastIndex) {
                db.delete(TABLE_NAME, "id = '${multiDelet[i].id}'", null)
            }

            val notes = ArrayList<Notes>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "title", "body", "toggle"),
                null, null, //where
                null, // group by
                null, //having
                null //order
            )

            if(cursor.count!=0) {
                cursor.moveToFirst()

                val idCol = cursor.getColumnIndex("id")
                val titleCol = cursor.getColumnIndex("title")
                val bodyCol = cursor.getColumnIndex("body")
                val toggleCol = cursor.getColumnIndex("toggle")

                do  {
                    val note = Notes(
                        cursor.getInt(idCol),
                        cursor.getString(titleCol),
                        cursor.getString(bodyCol),
                        cursor.getInt(toggleCol)
                    )
                    notes.add(0,note)
                } while (cursor.moveToNext())
                cursor.close()
            }
            return notes
        }

        fun deletS(db: SQLiteDatabase, v : Int?): ArrayList<Notes> {
            db.delete(TABLE_NAME, "id = '$v'", null)

            val notes = ArrayList<Notes>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "title", "body", "toggle"),
                null, null, //where
                null, // group by
                null, //having
                null //order
            )

            if(cursor.count!=0) {
                cursor.moveToFirst()

                val idCol = cursor.getColumnIndex("id")
                val titleCol = cursor.getColumnIndex("title")
                val bodyCol = cursor.getColumnIndex("body")
                val toggleCol = cursor.getColumnIndex("toggle")

                do  {
                    val note = Notes(
                        cursor.getInt(idCol),
                        cursor.getString(titleCol),
                        cursor.getString(bodyCol),
                        cursor.getInt(toggleCol)
                    )
                    notes.add(0,note)
                } while (cursor.moveToNext())
                cursor.close()
            }
            return notes
        }

        fun search(db: SQLiteDatabase, v : String): ArrayList<Notes> {

            val notes = ArrayList<Notes>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "title", "body", "toggle"),
                "title LIKE '%$v%' OR body LIKE '%$v%'", null, //where
                null, // group by
                null, //having
                null //order
            )

            if(cursor.count!=0) {
                cursor.moveToFirst()

                val idCol = cursor.getColumnIndex("id")
                val titleCol = cursor.getColumnIndex("title")
                val bodyCol = cursor.getColumnIndex("body")
                val toggleCol = cursor.getColumnIndex("toggle")

                do  {
                    val note = Notes(
                        cursor.getInt(idCol),
                        cursor.getString(titleCol),
                        cursor.getString(bodyCol),
                        cursor.getInt(toggleCol)
                    )
                    notes.add(note)
                } while (cursor.moveToNext())
                cursor.close()
            }
            return notes
        }

        fun getAllTasks(db: SQLiteDatabase): ArrayList<Notes> {

            val notes = ArrayList<Notes>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id", "title", "body", "toggle"),
                null, null, //where
                null, // group by
                null, //having
                null //order
            )

            if(cursor.count!=0) {
                cursor.moveToFirst()

                val idCol = cursor.getColumnIndex("id")
                val titleCol = cursor.getColumnIndex("title")
                val bodyCol = cursor.getColumnIndex("body")
                val toggleCol = cursor.getColumnIndex("toggle")

                do  {
                    val note = Notes(
                        cursor.getInt(idCol),
                        cursor.getString(titleCol),
                        cursor.getString(bodyCol),
                        cursor.getInt(toggleCol)
                    )
                    notes.add(0,note)
                } while (cursor.moveToNext())
                cursor.close()
            }
            return notes
        }
    }
}