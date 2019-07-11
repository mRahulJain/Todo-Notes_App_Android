package com.example.todo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_NAME = "tasks.db"
const val DB_VER = 1

class MyDbHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.apply {
            execSQL(TasksTable.CMD_CREATE_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}