package com.example.smarthomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.Adapter.RoutineItemAdapter
import com.example.smarthomeapp.Adapter.SelectRoutineAdapter
import com.example.smarthomeapp.Database.DatabaseHandler
import com.example.smarthomeapp.Models.RoutineModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SelectRoutine : AppCompatActivity() {
    private lateinit var rvSelectRoutinesList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_routine)

        val toolbar = findViewById<Toolbar>(R.id.selectRoutineToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        // Initialize views
        rvSelectRoutinesList = findViewById(R.id.rvSelectRoutinesList)

        val createRoutineButton  = findViewById<FloatingActionButton>(R.id.CreateRoutineFAB)

        createRoutineButton.setOnClickListener { createRoutine() }

        setupListofDataIntoRecyclerView()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }


    private fun createRoutine() {
        val intent = Intent(this, CreateRoutine::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Function is used to get the RoutinesItems List which is added in the database table.
     */

    private fun getItemsList(): ArrayList<RoutineModel> {

        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        //calling the viewRoutine method of the DatabaseHandler class to read the records
        val routinesList: ArrayList<RoutineModel> = databaseHandler.viewRoutine()

        return routinesList
    }

    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView(){
        if (getItemsList().size > 0){
            rvSelectRoutinesList.visibility = View.VISIBLE

            // Set the LayoutManager that this RecyclerView will use.
            rvSelectRoutinesList.layoutManager = LinearLayoutManager(this)

            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = SelectRoutineAdapter(this, getItemsList(), this)
            //adapter instance is set to the recyclerview to inflate the items
            rvSelectRoutinesList.adapter = itemAdapter

        }else{
            rvSelectRoutinesList.visibility = View.GONE

        }
    }

    fun addToFavorites(routine: RoutineModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add to Favorites")

        builder.setPositiveButton("OK") { dialog, _ ->

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.addFavoriteRoutine(
                RoutineModel(
                    routine.id, routine.routineName, routine.time, routine.notification, routine.location, routine.lastRun
                )
            )
            if (status > -1) {
                Toast.makeText(this, "Added to Favorites.", Toast.LENGTH_LONG).show()

                setupListofDataIntoRecyclerView()

                dialog.dismiss()// Dialog will be dismissed
            }else{
                Toast.makeText(
                    this, "Error adding routine to favorites", Toast.LENGTH_LONG
                ).show()
            }

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}