package com.example.smarthomeapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.*
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import com.example.smarthomeapp.Database.DatabaseHandler
import com.example.smarthomeapp.Models.RoutineModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class CreateRoutine : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var routineNameET: TextInputEditText
    private val SHARED_PREFS_KEY = "MySharedPreferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        val cancel: AppCompatImageView = findViewById(R.id.cancel_button)
        cancel.setOnClickListener {
            finish()
        }

        val addActionButton = findViewById<FloatingActionButton>(R.id.AddActionFAB)
        val addEventButton = findViewById<FloatingActionButton>(R.id.AddEventFAB)

        routineNameET = findViewById(R.id.etRoutineName)
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

        createNotificationChannel()


    }


    private fun addConditionViews() {
        val addConditionLayout = findViewById<TextView>(R.id.addConditionLayout)
        addConditionLayout.visibility = View.VISIBLE
        val addEventLayout = findViewById<TextView>(R.id.addEventLayout)
        addEventLayout.visibility = View.GONE

        val addConditionFABLayout = findViewById<LinearLayout>(R.id.addConditionFABLayout)
        addConditionFABLayout.visibility = View.VISIBLE
    }

    private fun getNotificationsSharedPreferences() {
        val notificationText = sharedPreferences.getString("NotificationPrefs", null)
        if (notificationText != null) {
            updateActionRowLayout(notificationText)
        }
    }

    private fun getRoutineSharedPreferences() {
        val routine = sharedPreferences.getString("routineName", null)
        routineNameET.setText(routine)
    }

    private fun getTimeSharedPreferences() {
        val timeText = sharedPreferences.getString("TimePrefs", null)
        if (timeText != null) {
            updateEventRow(timeText)
            addConditionViews()
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

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val hourText =
                    if (selectedHour == 0 || selectedHour == 12) "12" else (selectedHour % 12).toString()
                val minuteText = if (selectedMinute < 10) "0$selectedMinute" else "$selectedMinute"
                val amPmText = if (amPm == Calendar.AM) "AM" else "PM"
                val timeText = "$hourText:$minuteText $amPmText"

                updateEventRow(timeText)
                addConditionViews()

                // save the value to shared preferences
                val editor = sharedPreferences.edit()
                editor.putString("TimePrefs", timeText)
                editor.apply()

            }, hour, minute, false
        )

        timePickerDialog.show()
    }

    private fun updateEventRow(timeText: String) {
        val eventRowLayout = findViewById<LinearLayout>(R.id.event_row_layout)
        eventRowLayout.visibility = View.VISIBLE
        val selectedTimeTV = findViewById<TextView>(R.id.selectedTimeTV)
        selectedTimeTV.visibility = View.GONE

        val boldTimeText = formatText(timeText)

        // Construct the string with boldTimeText and regular text
        val timeText = SpannableStringBuilder()
        timeText.append("The time is ")
        timeText.append(boldTimeText)

        // Change the text in the text view with id @+id/tv_AddTime
        findViewById<TextView>(R.id.tv_AddTime).text = timeText
    }

    private fun formatText(text: String): CharSequence {
        // Create a new SpannableString with the time text in bold
        val spannableText = SpannableString(text)
        spannableText.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableText
    }


    private fun addEvent() {
        sharedPreferences.edit().putString("routineName", routineNameET.text.toString()).apply()
        val intent = Intent(this, SelectEvent::class.java)
        startActivity(intent)
    }

    private fun addAction() {
        sharedPreferences.edit().putString("routineName", routineNameET.text.toString()).apply()
        val intent = Intent(this, SelectThing::class.java)
        startActivity(intent)
    }
//requireContext is only in fragments
    // all occurrences CTRL + SHIFT + ALT + J

    private fun showInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter a value")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            val inputText = input.text.toString()

            updateActionRowLayout(inputText)

            // save the value to shared preferences
            val editor = sharedPreferences.edit()
            editor.putString("NotificationPrefs", inputText)
            editor.apply()

            showProcessingDialog(input.text.toString())

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun updateActionRowLayout(inputText: String) {
        val actionRowLayout = findViewById<LinearLayout>(R.id.action_row_layout)
        actionRowLayout.visibility = View.VISIBLE
        val actionsTV = findViewById<TextView>(R.id.actionsTV)
        actionsTV.visibility = View.GONE

        val boldNotificationText = formatText(inputText)

        // Construct the string with boldTimeText and regular text
        val notificationText = SpannableStringBuilder()
        notificationText.append("Send Notification: ")
        notificationText.append(boldNotificationText)


        // Change the text in the text view with id @+id/tv_AddNotification
        actionRowLayout.findViewById<TextView>(R.id.tv_AddNotification).text = notificationText
    }

    private fun showProcessingDialog(value: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Creating new routine")
        builder.setCancelable(false)

//        val progressBar = ProgressBar(this, null, android.R.style.Widget_DeviceDefault_Light_ProgressBar_Horizontal)
//        builder.setView(progressBar)

        val dialogLayout = layoutInflater.inflate(R.layout.dialog_custom_progress_bar, null)
        builder.setView(dialogLayout)

        // Start the progress bar animation
        val progressBar = dialogLayout.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.isIndeterminate = true
        progressBar.animate()

        val dialog = builder.create()
        dialog.show()

        addRoutineRecord()

        Handler().postDelayed({

            dialog.dismiss()
        }, 3000)

    }

    private fun addRoutineRecord() {
        val routine = sharedPreferences.getString("routineName", null)
        val time = sharedPreferences.getString("TimePrefs", null)
        val notification = sharedPreferences.getString("NotificationPrefs", null)

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (routine?.isNotEmpty() == true || time?.isNotEmpty() == true || notification?.isNotEmpty() == true) {

//            val status = databaseHandler.addRoutine(RoutineModel(0, routine, time, notification, "Current", "Never"))
            val status = databaseHandler.addRoutine(
                RoutineModel(
                    0,
                    routine.toString(),
                    time.toString(),
                    notification.toString(),
                    "Current",
                    "Never"
                )
            )

            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                scheduleNotification()
                sharedPreferences.edit().clear().apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("SELECTED_FRAGMENT", "routines")
                startActivity(intent)
            }
        } else {
            Toast.makeText(applicationContext, "Error creating routine", Toast.LENGTH_LONG).show()
        }
    }


    private fun scheduleNotification()
    {
        val intent = Intent(applicationContext, Notification::class.java)
        val routine = sharedPreferences.getString("routineName", null).toString()
        val notification = sharedPreferences.getString("NotificationPrefs", null).toString()
        intent.putExtra("routineExtra", routine)
        intent.putExtra("notificationExtra", notification)

        val notificationId = 1 // Unique identifier for the pending intent

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        Log.i("Time", time.toString())

        // Use setExact instead of setExactAndAllowWhileIdle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }

        showAlert(time, routine, notification)
    }

    private fun showAlert(time: Long, routine: String, notification: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + routine +
                        "\nMessage: " + notification +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    fun getTime(): Long {
        val timeString = sharedPreferences.getString("TimePrefs", null).toString()
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = dateFormat.parse(timeString)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.timeInMillis
    }


    private fun createNotificationChannel()
    {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channel1", name, importance)
            channel.description = desc

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }

}