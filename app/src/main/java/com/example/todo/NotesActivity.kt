package com.example.todo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.activity_notes2.*
import kotlinx.android.synthetic.main.list_item_notes.view.*

class NotesActivity : AppCompatActivity() {

    var notes = arrayListOf<NotesTable.Notes>()
    var dbHelper = NoteDbHelper(this)
    lateinit var notesDb : SQLiteDatabase


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        var count = intent.getStringExtra("count")

//        val prefs = getPreferences(Context.MODE_PRIVATE)
//        count = prefs.getInt("ACTIVITY_OPEN", count)
//        count++
//        prefs.edit {
//            putInt("ACTIVITY_OPEN", count)
//        }
//        Log.d("COUNT", "$count")

        Log.d("count", "OK : $count")


        notesDb = dbHelper.writableDatabase

        if(count=="NotMain") {
            val title = intent.getStringExtra("title")
            val body = intent.getStringExtra("body")
            NotesTable.insertTask(
                notesDb,
                NotesTable.Notes(
                    null,
                    title,
                    body
                )
            )
            notes = NotesTable.getAllTasks(notesDb)
            Log.d("DATA", "NOTES: $notes")
            rView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            rView.adapter = NoteAdapter(notes)
        }else {
            notes = NotesTable.getAllTasks(notesDb)
            rView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            rView.adapter = NoteAdapter(notes)
        }

        addNote.setOnClickListener {
            var intent = Intent(this@NotesActivity, Notes2Activity::class.java)
            startActivity(intent)
            finish()
        }

        deletAll.setOnClickListener {
            NotesTable.deletAll(notesDb)
            notes = NotesTable.getAllTasks(notesDb)
            rView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            rView.adapter = NoteAdapter(notes)
        }

        SeText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, end: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, end: Int) {
                if(s=="") {
                    notes = NotesTable.getAllTasks(notesDb)
                } else {
                    notes = NotesTable.search(notesDb, s.toString())
                }
                rView.layoutManager = GridLayoutManager(this@NotesActivity, 2, GridLayoutManager.VERTICAL, false)
                rView.adapter = NoteAdapter(notes)
            }
        })


    }

    inner class NoteAdapter(
        var notes : ArrayList<NotesTable.Notes>
    ): RecyclerView.Adapter<NoteAdapter.CourseViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.CourseViewHolder {
            val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val itemView = li.inflate(R.layout.list_item_notes, parent, false)
            return CourseViewHolder(itemView)

            //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemCount(): Int = notes.size

        @SuppressLint("WrongConstant")
        override fun onBindViewHolder(holder: NoteAdapter.CourseViewHolder, position: Int) {
            val note = notes[position]
            holder.itemView.title.text = note.title
            holder.itemView.body.text = note.body

            holder.itemView.deletS.setOnClickListener {
                var v = note.id
                notes = NotesTable.deletS(notesDb, v)
                rView.layoutManager = GridLayoutManager(this@NotesActivity, 2, GridLayoutManager.VERTICAL, false)
                rView.adapter = NoteAdapter(notes)
            }
        }

        inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}
