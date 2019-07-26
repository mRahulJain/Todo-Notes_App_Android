package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.activity_notes2.*

class Notes2Activity : AppCompatActivity() {

    lateinit var flag : String
    lateinit var head : String
    lateinit var content : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes2)



        flag = intent.getStringExtra("flag")
        if(flag=="true") {
            titleN.setText(intent.getStringExtra("title"))
            head = titleN.text.toString()
            body.setText(intent.getStringExtra("body"))
            content = body.text.toString()
        }

        done.setOnClickListener {
            var intent = Intent(this@Notes2Activity, NotesActivity::class.java)
            val title = titleN.text.toString()
            val body = body.text.toString()
            if(title=="" && body=="") {
                Toast.makeText(this, "Enter something", Toast.LENGTH_LONG).show()
            } else {
                intent.putExtra("title", title)
                intent.putExtra("body", body)
                intent.putExtra("count", "NotMain")
                startActivity(intent)
                finish()
            }
        }


    }

    override fun onBackPressed() {
        if(flag == "true") {
            var intent = Intent(this@Notes2Activity, NotesActivity::class.java)
            intent.putExtra("title", head)
            intent.putExtra("body", content)
            intent.putExtra("count", "NotMain")
            startActivity(intent)
            finish()
        } else {
            var intent = Intent(this@Notes2Activity, NotesActivity::class.java)
            intent.putExtra("count", "Main")
            startActivity(intent)
            finish()
        }
    }
}
