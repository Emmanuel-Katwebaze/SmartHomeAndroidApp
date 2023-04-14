package com.example.smarthomeapp

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class CreateRoutine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        val addActionButton = findViewById<FloatingActionButton>(R.id.AddActionFAB)
        val addEventButton = findViewById<FloatingActionButton>(R.id.AddEventFAB)

        addActionButton.setOnClickListener { addAction() }
        addEventButton.setOnClickListener { addEvent() }

        val notificationCreated = intent.getBooleanExtra("notificationCreated", false)
        if (notificationCreated) {
            showInputDialog()
            intent.putExtra("notificationCreated", false) // set notificationCreated value to false
        }

        val timeSet = intent.getBooleanExtra("timeSet", false)
        if (timeSet) {
            showTimePickerDialog()
            intent.putExtra("timeSet", false) // set timeSet value to false
        }


    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = calendar.get(Calendar.AM_PM)

        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val hourText = if (selectedHour == 0 || selectedHour == 12) "12" else (selectedHour % 12).toString()
                val minuteText = if (selectedMinute < 10) "0$selectedMinute" else "$selectedMinute"
                val amPmText = if (amPm == Calendar.AM) "AM" else "PM"
                val timeText = "Date & Time: The time is $hourText:$minuteText $amPmText"
                // Show the selected time in a text view
                findViewById<TextView>(R.id.selectedTimeTV).text = timeText
            }, hour, minute, false
        )

        timePickerDialog.show()
    }


    private fun addEvent() {
        val intent = Intent(this, SelectEvent::class.java)
        startActivity(intent)
    }

    private fun addAction() {
        val intent = Intent(this, SelectThing::class.java)
        startActivity(intent)
    }
//requireContext is only in fragments
    // all occurences CTRL + SHIFT + ALT + J

    private fun showInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter a value")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            showProcessingDialog(input.text.toString())
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showProcessingDialog(value: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Creating new routine")
        builder.setCancelable(false)

        val progressBar = ProgressBar(this)
        builder.setView(progressBar)

        val dialog = builder.create()
        dialog.show()

        //TODO: Replace this with the logic to create a new routine using the entered value
        Handler().postDelayed({
            dialog.dismiss()
        }, 3000)
    }
}