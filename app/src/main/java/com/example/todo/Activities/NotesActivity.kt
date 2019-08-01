package com.example.todo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.list_item_notes.view.*

class NotesActivity : AppCompatActivity() {

    var notes = arrayListOf<NotesTable.Notes>()
    var notesL = arrayListOf<NotesTable.Notes>()
    var notesR = arrayListOf<NotesTable.Notes>()
    var dbHelper = NoteDbHelper(this)
    lateinit var notesDb : SQLiteDatabase
    lateinit var multiDelet : ArrayList<NotesTable.Notes>

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        var count = intent.getStringExtra("count")

        notesDb = dbHelper.writableDatabase


        deletAll.isVisible = false

        if(count=="NotMain") {
            val title = intent.getStringExtra("title")
            val body = intent.getStringExtra("body")
                NotesTable.insertTask(
                    notesDb,
                    NotesTable.Notes(
                        null,
                        title,
                        body,
                        0
                    )
                )
        }
        notes = NotesTable.getAllTasks(notesDb)
        notesL.clear()
        notesR.clear()
        var i = 0
        for(note in notes) {
            if(i%2==0) {
                notesL.add(note)
                i++
            } else {
                notesR.add(note)
                i++
            }
        }

        rViewL.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        rViewL.adapter = NoteAdapter(notesL)

        rViewR.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        rViewR.adapter = NoteAdapter(notesR)

        addNote.setOnClickListener {
            var intent = Intent(this@NotesActivity, Notes2Activity::class.java)
            intent.putExtra("flag", "false")
            startActivity(intent)
            finish()
        }

        deletAll.setOnClickListener {
            notes = NotesTable.deletAll(multiDelet, notesDb)
            notesL.clear()
            notesR.clear()
            var i = 0
            for(note in notes) {
                if(i%2==0) {
                    notesL.add(note)
                    i++
                } else {
                    notesR.add(note)
                    i++
                }
            }
            rViewL.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            rViewL.adapter = NoteAdapter(notesL)

            rViewR.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            rViewR.adapter = NoteAdapter(notesR)
            deletAll.isVisible = false
        }

        SeText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, end: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, end: Int) {
                deletAll.isVisible = false
                if(s=="") {
                    notes = NotesTable.getAllTasks(notesDb)
                } else {
                    notes = NotesTable.search(notesDb, s.toString())
                }
                notesL.clear()
                notesR.clear()
                var i = 0
                for(note in notes) {
                    if(i%2==0) {
                        notesL.add(note)
                        i++
                    } else {
                        notesR.add(note)
                        i++
                    }
                }
                rViewL.layoutManager = GridLayoutManager(this@NotesActivity, 1, GridLayoutManager.VERTICAL, false)
                rViewL.adapter = NoteAdapter(notesL)

                rViewR.layoutManager = GridLayoutManager(this@NotesActivity, 1, GridLayoutManager.VERTICAL, false)
                rViewR.adapter = NoteAdapter(notesR)
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
                notesL.clear()
                notesR.clear()
                var i = 0
                for(note in notes) {
                    if(i%2==0) {
                        notesL.add(note)
                        i++
                    } else {
                        notesR.add(note)
                        i++
                    }
                }
                rViewL.layoutManager = GridLayoutManager(this@NotesActivity, 1, GridLayoutManager.VERTICAL, false)
                rViewL.adapter = NoteAdapter(notesL)

                rViewR.layoutManager = GridLayoutManager(this@NotesActivity, 1, GridLayoutManager.VERTICAL, false)
                rViewR.adapter = NoteAdapter(notesR)
            }

            holder.itemView.parentLayout.setOnClickListener {
                if(deletAll.isVisible) {
                    if(note.toggle == 0) {
                        multiDelet.add(note)
                        holder.itemView.title.setBackgroundColor(Color.GRAY)
                        holder.itemView.body.setBackgroundColor(Color.GRAY)
                        holder.itemView.deletS.setBackgroundColor(Color.GRAY)
                        note.toggle = 1
                    } else {
                        multiDelet.remove(note)
                        holder.itemView.title.setBackgroundColor(Color.WHITE)
                        holder.itemView.body.setBackgroundColor(Color.WHITE)
                        holder.itemView.deletS.setBackgroundColor(Color.WHITE)
                        if(multiDelet.size == 0) {
                            deletAll.isVisible = false
                        }
                        note.toggle = 0
                    }
                } else {
                    var intent = Intent(this@NotesActivity, Notes2Activity::class.java)
                    intent.putExtra("title", note.title)
                    intent.putExtra("body", note.body)
                    intent.putExtra("flag", "true")
                    var v = note.id
                    NotesTable.deletS(notesDb, v)
                    startActivity(intent)
                    finish()
                }
            }

            holder.itemView.parentLayout.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(p0: View?): Boolean {
                    multiDelet = returnList(position)
                    note.toggle = 1
                    deletAll.isVisible = true
                    holder.itemView.title.setBackgroundColor(Color.GRAY)
                    holder.itemView.body.setBackgroundColor(Color.GRAY)
                    holder.itemView.deletS.setBackgroundColor(Color.GRAY)
                    return true
                }

            })
        }

        fun returnList(position : Int) : ArrayList<NotesTable.Notes> {
            val note = notes[position]
            val list = ArrayList<NotesTable.Notes>()
            list.add(note)
            return list
        }

        inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}
