package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.activity_notes2.*

class Notes2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes2)

        done.setOnClickListener {
            var intent = Intent(this@Notes2Activity, NotesActivity::class.java)
            val title = titleN.text.toString()
            val body = body.text.toString()
            if(title=="" && body=="") {
                Toast.makeText(this, "Enter something", Toast.LENGTH_LONG).show()
            } else {
                intent.putExtra("title", title)
                intent.putExtra("body", body)
                startActivity(intent)
                finish()
            }
        }
    }
}
