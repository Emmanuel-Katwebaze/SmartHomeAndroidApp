package com.example.smarthomeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.Adapter.FavoritesAdapter
import com.example.smarthomeapp.Adapter.RoutineItemAdapter
import com.example.smarthomeapp.Database.DatabaseHandler
import com.example.smarthomeapp.Models.RoutineModel
import com.example.smarthomeapp.R
import com.example.smarthomeapp.SelectRoutine
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {
    private lateinit var rvFavoritesList: RecyclerView
    private lateinit var tv_ActiveFavoritesHd: TextView
    private lateinit var noFavoritesLayout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        val favoritesFAB = view?.findViewById<FloatingActionButton>(R.id.idFavoritesFAB)

        favoritesFAB?.setOnClickListener { selectRoutine() }

        // Initialize views
        rvFavoritesList = view.findViewById(R.id.rvFavoritesList)
        tv_ActiveFavoritesHd = view.findViewById(R.id.tv_ActiveFavoritesHd)
        noFavoritesLayout = view.findViewById(R.id.noFavoritesLayout)

        setupListofDataIntoRecyclerView()

        return view
    }


    private fun selectRoutine() {
        val intent = Intent(activity, SelectRoutine::class.java)
        startActivity(intent)
    }

    private fun setupListofDataIntoRecyclerView() {
        if (getItemsList().size > 0) {
            rvFavoritesList.visibility = View.VISIBLE
            tv_ActiveFavoritesHd.visibility = View.VISIBLE
            noFavoritesLayout.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            // for activities -> rvFavoritesList.layoutManager = LinearLayoutManager(this)

            rvFavoritesList.layoutManager = LinearLayoutManager(requireContext())

            // Adapter class is initialized and list is passed in the param.
            // for activites -> val itemArray = RoutineItemAdapter(this, getItemsList())

            val itemAdapter = FavoritesAdapter(requireContext(), getItemsList(), this)
            //adapter instance is set to the recyclerview to inflate the items
            rvFavoritesList.adapter = itemAdapter

        } else {
            rvFavoritesList.visibility = View.GONE
            tv_ActiveFavoritesHd.visibility = View.GONE
            noFavoritesLayout.visibility = View.VISIBLE

        }
    }

    private fun getItemsList(): ArrayList<RoutineModel> {
        //creating the instance of DatabaseHandler class
        // for activities - > val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

        //calling the viewRoutine method of the DatabaseHandler class to read the records
        val favoritesList: ArrayList<RoutineModel> = databaseHandler.viewFavorites()

        return favoritesList
    }

    fun deleteFromFavorites(routine: RoutineModel) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Remove From Favorites?")

        builder.setPositiveButton("OK") { dialog, _ ->

            val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

            //calling the deleteFavorite method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteFavorite(RoutineModel(routine.id, "", "", "", "", ""))
            if (status > -1) {
                Toast.makeText(
                    requireContext(), "Favorite removed successfully.", Toast.LENGTH_LONG
                ).show()
                setupListofDataIntoRecyclerView()
                dialog.dismiss()// Dialog will be dismissed
            }else{
                Toast.makeText(
                    requireContext(), "Error removing favorite", Toast.LENGTH_LONG
                ).show()
            }

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

}

