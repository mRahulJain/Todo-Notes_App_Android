package com.example.todo

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(context: MainActivity) : DialogFragment() {
    val req = context

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)
        return TimePickerDialog(
            req,
            activity as TimePickerDialog.OnTimeSetListener,
            hour,
            minute,
            android.text.format.DateFormat.is24HourFormat(req))
    }
}