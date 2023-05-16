package com.example.smarthomeapp.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.Adapter.RoutineItemAdapter
import com.example.smarthomeapp.Database.DatabaseHandler
import com.example.smarthomeapp.Models.RoutineModel
import com.example.smarthomeapp.R
import java.util.*
import kotlin.collections.ArrayList


class RoutinesFragment : Fragment() {
    private lateinit var rvRoutinesList: RecyclerView
    private lateinit var tv_ActiveRoutinesHd: TextView
    private lateinit var noRoutinesLayout: RelativeLayout

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

        setupListofDataIntoRecyclerView()
        return view
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
        val updateDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.update_routine_row)


        val etUpdateName = updateDialog.findViewById<EditText>(R.id.etUpdateName)
        val etUpdateTime = updateDialog.findViewById<EditText>(R.id.etUpdateTime)
        val etUpdateNotification = updateDialog.findViewById<EditText>(R.id.etUpdateNotification)
        val tvUpdate = updateDialog.findViewById<TextView>(R.id.tvUpdate)
        val tvCancel = updateDialog.findViewById<TextView>(R.id.tvCancel)
        val tvDelete = updateDialog.findViewById<TextView>(R.id.tvDelete)

        etUpdateName.setText(routine.routineName)
        etUpdateTime.setText(routine.time)
        etUpdateNotification.setText(routine.notification)

        etUpdateTime.setOnClickListener{
            showTimePickerDialog(etUpdateTime)
        }

        tvUpdate.setOnClickListener(View.OnClickListener {

            val name = etUpdateName.text.toString()
            val time = etUpdateTime.text.toString()
            val notification = etUpdateNotification.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

            if (!name.isEmpty() && !time.isEmpty() && !notification.isEmpty()) {
                val status = databaseHandler.updateRoutine(
                    RoutineModel(
                        routine.id, name, time, notification, "Current", routine.lastRun
                    )
                )
                if (status > -1) {
                    Toast.makeText(requireContext(), "Record Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    requireContext(), "Error updating record", Toast.LENGTH_LONG
                ).show()
            }
        })

        tvDelete.setOnClickListener {
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteRoutine(RoutineModel(routine.id, "", "", "", "", ""))
            if (status > -1) {
                Toast.makeText(
                    requireContext(), "Record deleted successfully.", Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
                updateDialog.dismiss()
            }

            Toast.makeText(
                requireContext(), "Error deleting record", Toast.LENGTH_LONG
            ).show()
        }

        tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })

        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    private fun showTimePickerDialog(editText: EditText) {
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

                editText.setText(timeText)

            }, hour, minute, false
        )

        timePickerDialog.show()
    }


}