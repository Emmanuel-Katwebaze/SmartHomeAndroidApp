package com.example.smarthomeapp.fragments


import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.Adapter.RoutineItemAdapter
import com.example.smarthomeapp.CreateRoutine
import com.example.smarthomeapp.Database.DatabaseHandler
import com.example.smarthomeapp.GoogleMaps
import com.example.smarthomeapp.Models.RoutineModel
import com.example.smarthomeapp.R
import com.example.smarthomeapp.SelectRoutine
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList


class RoutinesFragment : Fragment() {
    private lateinit var rvRoutinesList: RecyclerView
    private lateinit var tv_ActiveRoutinesHd: TextView
    private lateinit var noRoutinesLayout: RelativeLayout
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    // Declare updateDialog as a public variable
    private var updateDialog: Dialog? = null


    // Request code for the other activity
    private val OTHER_ACTIVITY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_routines, container, false)


        // Initialize views
        rvRoutinesList = view.findViewById(R.id.rvRoutinesList)
        tv_ActiveRoutinesHd = view.findViewById(R.id.tv_ActiveRoutinesHd)
        noRoutinesLayout = view.findViewById(R.id.noRoutinesLayout)

        val favoritesFAB = view?.findViewById<FloatingActionButton>(R.id.idFABAddRoutine)

        favoritesFAB?.setOnClickListener { getMaps() }

        setupListofDataIntoRecyclerView()

        // Create the result launcher
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Retrieve the result data from the intent
                    val data: Intent? = result.data
                    val result = data?.getStringExtra("updateLocation")
                    val etUpdateLocation = updateDialog?.findViewById<TextView>(R.id.etUpdateLocation)
                    etUpdateLocation?.text = result.toString()

                }
            }

        return view
    }

    private fun getMaps() {
        val intent = Intent(activity, CreateRoutine::class.java)
        startActivity(intent)
    }


    /**
     * Function is used to get the RoutinesItems List which is added in the database table.
     */

    private fun getItemsList(): ArrayList<RoutineModel> {
        //creating the instance of DatabaseHandler class
        // for activities - > val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

        //calling the viewRoutine method of the DatabaseHandler class to read the records
        val routinesList: ArrayList<RoutineModel> = databaseHandler.viewRoutine()

        return routinesList
    }

    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {
        if (getItemsList().size > 0) {
            rvRoutinesList.visibility = View.VISIBLE
            tv_ActiveRoutinesHd.visibility = View.VISIBLE
            noRoutinesLayout.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            // for activities -> rvRoutinesList.layoutManager = LinearLayoutManager(this)

            rvRoutinesList.layoutManager = LinearLayoutManager(requireContext())

            // Adapter class is initialized and list is passed in the param.
            // for activites -> val itemArray = RoutineItemAdapter(this, getItemsList())

            val itemAdapter = RoutineItemAdapter(requireContext(), getItemsList(), this)
            //adapter instance is set to the recyclerview to inflate the items
            rvRoutinesList.adapter = itemAdapter

        } else {
            rvRoutinesList.visibility = View.GONE
            tv_ActiveRoutinesHd.visibility = View.GONE
            noRoutinesLayout.visibility = View.VISIBLE

        }
    }

    /**
     * Method is used to show the Custom Dialog.
     */
    fun updateRecordDialog(routine: RoutineModel) {
        updateDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        updateDialog?.setCancelable(false)

        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog?.setContentView(R.layout.update_routine_row)


        val etUpdateName = updateDialog?.findViewById<EditText>(R.id.etUpdateName)
        val etUpdateTime = updateDialog?.findViewById<TextView>(R.id.etUpdateTime)
        val etUpdateLocation = updateDialog?.findViewById<TextView>(R.id.etUpdateLocation)
        val etUpdateNotification = updateDialog?.findViewById<EditText>(R.id.etUpdateNotification)

        val tvUpdate = updateDialog?.findViewById<TextView>(R.id.tvUpdate)
        val tvCancel = updateDialog?.findViewById<TextView>(R.id.tvCancel)
        val tvDelete = updateDialog?.findViewById<TextView>(R.id.tvDelete)

        etUpdateName?.setText(routine.routineName)
        etUpdateTime?.text = routine.time
        etUpdateLocation?.text = routine.location
        etUpdateNotification?.setText(routine.notification)

        etUpdateTime?.setOnClickListener {
            showTimePickerDialog(etUpdateTime)
        }

        etUpdateLocation?.setOnClickListener {
            val intent = Intent(requireContext(), GoogleMaps::class.java)
            intent.putExtra("updateLocation", "update")
            resultLauncher.launch(intent)
        }

        tvUpdate?.setOnClickListener(View.OnClickListener {

            val name = etUpdateName?.text.toString()
            val time = etUpdateTime?.text.toString()
            val notification = etUpdateNotification?.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

            if (!name.isEmpty() && !time.isEmpty() && !notification.isEmpty()) {
                val status = databaseHandler.updateRoutine(
                    RoutineModel(
                        routine.id, name, time, notification, "Current", routine.lastRun
                    )
                )
                if (status > -1) {
                    Toast.makeText(requireContext(), "Routine Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog?.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    requireContext(), "Error updating record", Toast.LENGTH_LONG
                ).show()
            }
        })

        tvDelete?.setOnClickListener {
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteRoutine(RoutineModel(routine.id, "", "", "", "", ""))
            if (status > -1) {
                Toast.makeText(
                    requireContext(), "Record deleted successfully.", Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
                updateDialog?.dismiss()
            }

            Toast.makeText(
                requireContext(), "Error deleting record", Toast.LENGTH_LONG
            ).show()
        }

        tvCancel?.setOnClickListener(View.OnClickListener {
            updateDialog?.dismiss()
        })

        //Start the dialog and display it on screen.
        updateDialog?.show()
    }

    private fun showTimePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = calendar.get(Calendar.AM_PM)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val hourText =
                    if (selectedHour == 0 || selectedHour == 12) "12" else (selectedHour % 12).toString()
                val minuteText = if (selectedMinute < 10) "0$selectedMinute" else "$selectedMinute"
                val amPmText = if (amPm == Calendar.AM) "AM" else "PM"
                val timeText = "$hourText:$minuteText $amPmText"

                textView.text = timeText

            }, hour, minute, false
        )

        timePickerDialog.show()
    }


}