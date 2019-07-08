package com.example.todo

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*

class MainActivity : AppCompatActivity() {

    var tasks = arrayListOf<TasksTable.Task>()
    var positionReq : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = MyDbHelper(this)
        val tasksDb = dbHelper.writableDatabase

        tasks = TasksTable.getAllTasks(tasksDb)
        val taskAdapter = TaskAdapter(tasks)
        todoView.adapter = taskAdapter

        btnAdd.setOnClickListener {
            val data = eText.text.toString()
            if(data == "") {
                Toast.makeText(this, "Please enter your todo!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            TasksTable.insertTask(
                tasksDb,
                TasksTable.Task(
                    null,
                    eText.text.toString(),
                    false
                )
            )
            tasks = TasksTable.getAllTasks(tasksDb)
            taskAdapter.updateTasks(tasks)
            eText.setText("")
        }

        btnDelet.setOnClickListener {
            TasksTable.deletTask(tasksDb)
            tasks = TasksTable.getAllTasks(tasksDb)
            taskAdapter.updateTasks(tasks)
        }

        btnSort.setOnClickListener {
            tasks = TasksTable.sortTask(tasksDb)
            taskAdapter.updateTasks(tasks)
        }

        eTextS.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, end: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, end: Int) {
                if(s=="") {
                    tasks = TasksTable.getAllTasks(tasksDb)
                    taskAdapter.updateTasks(tasks)
                } else {
                    tasks = TasksTable.search(tasksDb, s.toString())
                    taskAdapter.updateTasks(tasks)
                }
            }

        })

//        btnD.setOnClickListener {
//            val thisTask = taskAdapter.getItem(positionReq)
//            var v = thisTask.id
//            tasks = TasksTable.deletSTask(tasksDb, v)
//            taskAdapter.updateTasks(tasks)
//
//        }

        todoView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                positionReq = position
                val thisTask = taskAdapter.getItem(position)
                thisTask.done = !thisTask.done
                TasksTable.updateTask(tasksDb, thisTask)
                tasks = TasksTable.getAllTasks(tasksDb)
                taskAdapter.updateTasks(tasks)
            }

        }
    }

    class TaskAdapter(var tasks: ArrayList<TasksTable.Task>): BaseAdapter() {

        fun updateTasks(newTasks: ArrayList<TasksTable.Task>) {
            tasks.clear()
            tasks.addAll(newTasks)
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val li = parent!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = li.inflate(R.layout.list_item, parent, false)
            view.findViewById<TextView>(R.id.tView).text = getItem(position).task
            if(getItem(position).done) {
                view.findViewById<TextView>(R.id.tView).setTextColor(Color.GRAY)
            } else {
                view.findViewById<TextView>(R.id.tView).setTextColor(Color.BLACK)
            }

            return view
        }

        override fun getItem(position: Int): TasksTable.Task = tasks[position]

        override fun getItemId(position: Int): Long  = 0

        override fun getCount(): Int = tasks.size

    }
}
