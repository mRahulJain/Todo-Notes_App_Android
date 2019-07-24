package com.example.todo

import android.annotation.TargetApi
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var tasks = arrayListOf<TasksTable.Task>()
    var dbHelper = MyDbHelper(this)
    lateinit var tasksDb : SQLiteDatabase

    val calendar = Calendar.getInstance()
    val am by lazy {
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    var pi : PendingIntent? = null

    override fun onTimeSet(timePicker: TimePicker?, hour: Int, minute: Int) {
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        Snackbar.make(parentL, "Notification set for : " +
                "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH)}/" +
                "${calendar.get(Calendar.DAY_OF_MONTH)} at ${calendar.get(Calendar.HOUR_OF_DAY)}:" +
                "${calendar.get(Calendar.MINUTE)}", Snackbar.LENGTH_LONG).show()

        nonRepeatingAlarms(calendar)

        Log.d("DATEENTERED", "${calendar.get(Calendar.HOUR_OF_DAY)} : ${calendar.get(Calendar.MINUTE)}")
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, date: Int) {
        calendar.set(Calendar.YEAR , year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, date)

        val d = DateFormat.getDateInstance().format(calendar.time)

        val timePicker = TimePickerFragment(this@MainActivity)
        timePicker.show(supportFragmentManager, "Time picker")
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun nonRepeatingAlarms(calendar: Calendar) {
        val intent = Intent(this@MainActivity, AlarmReceiver::class.java)
        pi = PendingIntent.getBroadcast(
            this@MainActivity,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        am.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pi
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancelAll()

        tasksDb = dbHelper.writableDatabase

        tasks = TasksTable.getAllTasks(tasksDb)
        val taskAdapter = TaskAdapter(tasks)

        todoView.adapter = taskAdapter

        notes.setOnClickListener {
            var intent = Intent(this, NotesActivity::class.java)
            intent.putExtra("count", "Main")
            startActivity(intent)
        }

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

        setAlarm.setOnClickListener {
                val datePicker = DatePickerFragment(this@MainActivity)
                datePicker.show(supportFragmentManager, "Date picker")
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
                    Log.d("Task", "TASK : $tasks")
                    taskAdapter.updateTasks(tasks)
                }
            }
        })

        todoView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val thisTask = taskAdapter.getItem(position)
                thisTask.done = !thisTask.done
                check.isChecked = thisTask.done
                TasksTable.updateTask(tasksDb, thisTask)
                tasks = TasksTable.getAllTasks(tasksDb)
                taskAdapter.updateTasks(tasks)
            }
        }
    }

    inner class TaskAdapter(var tasksO: ArrayList<TasksTable.Task>): BaseAdapter() {

        fun updateTasks(newTasks: ArrayList<TasksTable.Task>) {
            tasksO.clear()
            tasksO.addAll(newTasks)
            notifyDataSetChanged()
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val li = parent!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = li.inflate(R.layout.list_item, parent, false)
            view.findViewById<TextView>(R.id.tView).text = getItem(position).task
            if(getItem(position).done) {
                view.findViewById<TextView>(R.id.tView).setTextColor(Color.GRAY)
                view.findViewById<CheckBox>(R.id.check).isChecked = true
            } else {
                view.findViewById<TextView>(R.id.tView).setTextColor(Color.BLACK)
                view.findViewById<CheckBox>(R.id.check).isChecked = false
            }

            view.findViewById<ImageButton>(R.id.btnD).setOnClickListener {
                    var v = getItem(position).id
                    tasks = TasksTable.deletSTask(tasksDb , v)
                    updateTasks(tasks)
            }

            return view
        }
        override fun getItem(position: Int): TasksTable.Task = tasks[position]
        override fun getItemId(position: Int): Long  = 0
        override fun getCount(): Int = tasks.size

    }
}
