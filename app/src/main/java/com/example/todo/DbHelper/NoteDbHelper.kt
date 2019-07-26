package com.example.todo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_NAME1 = "notes.db"
const val DB_VER1 = 2

class NoteDbHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME1, null, DB_VER1) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.apply {
            execSQL(NotesTable.CMD_CREATE_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}