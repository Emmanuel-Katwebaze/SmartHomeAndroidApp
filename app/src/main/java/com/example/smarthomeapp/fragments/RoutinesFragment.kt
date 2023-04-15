package com.example.smarthomeapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.Adapter.RoutineItemAdapter
import com.example.smarthomeapp.Database.DatabaseHandler
import com.example.smarthomeapp.Models.RoutineModel
import com.example.smarthomeapp.R


class RoutinesFragment : Fragment() {
    private lateinit var rvRoutinesList: RecyclerView
    private lateinit var tv_ActiveRoutinesHd: TextView
    private lateinit var noRoutinesLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
    private fun setupListofDataIntoRecyclerView(){
        if (getItemsList().size > 0){
            rvRoutinesList.visibility = View.VISIBLE
            tv_ActiveRoutinesHd.visibility = View.VISIBLE
            noRoutinesLayout.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            // for activities -> rvRoutinesList.layoutManager = LinearLayoutManager(this)

            rvRoutinesList.layoutManager = LinearLayoutManager(requireContext())

            // Adapter class is initialized and list is passed in the param.
           // for activites -> val itemArray = RoutineItemAdapter(this, getItemsList())

            val itemAdapter = RoutineItemAdapter(requireContext(), getItemsList())
            //adapter instance is set to the recyclerview to inflate the items
            rvRoutinesList.adapter = itemAdapter

        }else{
            rvRoutinesList.visibility = View.GONE
            tv_ActiveRoutinesHd.visibility = View.GONE
            noRoutinesLayout.visibility = View.VISIBLE

        }
    }

}