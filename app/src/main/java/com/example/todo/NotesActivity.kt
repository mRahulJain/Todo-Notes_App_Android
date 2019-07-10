package com.example.todo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.activity_notes2.*
import kotlinx.android.synthetic.main.list_item_notes.view.*

class NotesActivity : AppCompatActivity() {

    var notes = arrayListOf<NotesTable.Notes>()

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        val prefs = getPreferences(Context.MODE_PRIVATE)
        count = prefs.getInt("ACTIVITY_OPEN", 0)
        count++
        prefs.edit {
            putInt("ACTIVITY_OPEN", count)
        }
        Log.d("COUNT", "$count")

        var dbHelper = NoteDbHelper(this)
        val notesDb = dbHelper.writableDatabase

        if(count%2==0) {
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
            rView.layoutManager = LinearLayoutManager(this)
            rView.adapter = NoteAdapter(notes)
        }else {
            notes = NotesTable.getAllTasks(notesDb)
            rView.layoutManager = LinearLayoutManager(this)
            rView.adapter = NoteAdapter(notes)
        }

        addNote.setOnClickListener {
            var intent = Intent(this@NotesActivity, Notes2Activity::class.java)
            startActivity(intent)
            finish()
        }


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

        override fun onBindViewHolder(holder: NoteAdapter.CourseViewHolder, position: Int) {
            val note = notes[position]
            holder.itemView.title.text = note.title
            holder.itemView.body.text = note.body
        }

//        fun updateNotes(newNotes : ArrayList<NotesTable.Notes>) {
//            notes.clear()
//            notes.addAll(newNotes)
//            notifyDataSetChanged()
//        }

        inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}
