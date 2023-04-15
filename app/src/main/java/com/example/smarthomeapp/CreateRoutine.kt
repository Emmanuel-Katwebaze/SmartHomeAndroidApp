package com.example.smarthomeapp

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.smarthomeapp.Database.DatabaseHandler
import com.example.smarthomeapp.Models.RoutineModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class CreateRoutine : AppCompatActivity() {
    //private lateinit var actionRowLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var routineNameET: EditText
    private val SHARED_PREFS_KEY = "MySharedPreferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        val addActionButton = findViewById<FloatingActionButton>(R.id.AddActionFAB)
        val addEventButton = findViewById<FloatingActionButton>(R.id.AddEventFAB)
        routineNameET  = findViewById(R.id.etRoutineName)
        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)

        // Getting the various shared preferences on page load
        getTimeSharedPreferences()
        getRoutineSharedPreferences()
        getNotificationsSharedPreferences()


        //setting on click listeners to the various buttons
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

//        routineNameET.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                //do nothing
//            }
//
//            override fun onTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                //do nothing
//            }
//
//            override fun afterTextChanged(string: Editable?) {
//                // Save the entered text to SharedPreferences
//                sharedPreferences.edit().putString("routineName", string.toString()).apply()
//            }
//        })

    }

    private fun getNotificationsSharedPreferences() {
        val notificationText = sharedPreferences.getString("NotificationPrefs", null)
        if (notificationText != null) {
            val actionRowLayout = layoutInflater.inflate(R.layout.action_row, null)
            actionRowLayout.findViewById<TextView>(R.id.tv_AddNotification).text = "Send Notification: $notificationText"
            val container = findViewById<ViewGroup>(R.id.actionsTVContainer)
            val actionsTV = findViewById<TextView>(R.id.actionsTV)
            val index = container.indexOfChild(actionsTV)
            container.removeView(actionsTV)
            container.addView(actionRowLayout, index)
            actionRowLayout.id = R.id.actionsTV
        }
    }

    private fun getRoutineSharedPreferences() {
        val routine = sharedPreferences.getString("routineName", null)
        routineNameET.setText(routine)
    }

    private fun getTimeSharedPreferences() {
        val timeText = sharedPreferences.getString("TimePrefs", null)
        if (timeText != null) {
            val eventRowLayout = layoutInflater.inflate(R.layout.event_row, null)
            val selectedTimeTVContainer = findViewById<ViewGroup>(R.id.selectedTimeTVContainer)
            val selectedTimeTV = findViewById<TextView>(R.id.selectedTimeTV)
            val index = selectedTimeTVContainer.indexOfChild(selectedTimeTV)
            selectedTimeTVContainer.removeView(selectedTimeTV)
            selectedTimeTVContainer.addView(eventRowLayout, index)
            eventRowLayout.id = R.id.selectedTimeTV
            findViewById<TextView>(R.id.tv_AddTime).text = "The time is $timeText"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.edit().clear().apply()
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
                val timeText = "$hourText:$minuteText $amPmText"

                // Inflate the LinearLayout resource file
                val eventRowLayout = layoutInflater.inflate(R.layout.event_row, null)
                // Update the text of the TextView in the LinearLayout
               // eventRowLayout.findViewById<TextView>(R.id.tv_AddTime).text = "The time is $timeText"
                // Replace the TextView with id @+id/selectedTimeTV with the inflated LinearLayout
                val selectedTimeTVContainer = findViewById<ViewGroup>(R.id.selectedTimeTVContainer)
                val selectedTimeTV = findViewById<TextView>(R.id.selectedTimeTV)
                val index = selectedTimeTVContainer.indexOfChild(selectedTimeTV)
                selectedTimeTVContainer.removeView(selectedTimeTV)
                selectedTimeTVContainer.addView(eventRowLayout, index)
                eventRowLayout.id = R.id.selectedTimeTV // Set the ID of the inflated LinearLayout to the ID of the TextView that was removed

                // Change the text in the text view with id @+id/tv_AddTime
                findViewById<TextView>(R.id.tv_AddTime).text = "The time is $timeText"


                // save the value to shared preferences
                val editor = sharedPreferences.edit()
                editor.putString("TimePrefs", timeText)
                editor.apply()


            }, hour, minute, false
        )

        timePickerDialog.show()
    }


    private fun addEvent() {
        sharedPreferences.edit().putString("routineName", routineNameET.text.toString()).apply()
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
            val inputText = input.text.toString()

            val actionRowLayout = layoutInflater.inflate(R.layout.action_row, null)
            actionRowLayout.findViewById<TextView>(R.id.tv_AddNotification).text = "Send Notification: $inputText"
            val container = findViewById<ViewGroup>(R.id.actionsTVContainer)
            val actionsTV = findViewById<TextView>(R.id.actionsTV)
            val index = container.indexOfChild(actionsTV)
            container.removeView(actionsTV)
            container.addView(actionRowLayout, index)
            actionRowLayout.id = R.id.actionsTV


            // save the value to shared preferences
            val editor = sharedPreferences.edit()
            editor.putString("NotificationPrefs", inputText)
            editor.apply()
//            container.removeAllViews()
//            container.addView(actionRowLayout)

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
//        Handler().postDelayed({
//            dialog.dismiss()
//        }, 3000)

        addRoutineRecord()
    }

    private fun addRoutineRecord() {
        val routine = sharedPreferences.getString("routineName", null)
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(routine?.isNotEmpty() == true){
            val status = databaseHandler.addRoutine(RoutineModel(0, routine,"Never"))
            if (status > -1){
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                sharedPreferences.edit().clear().apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("SELECTED_FRAGMENT", "routines")
                startActivity(intent)
            }
        }else{
            Toast.makeText(applicationContext, "Error creating routine", Toast.LENGTH_LONG).show()
        }
    }
}