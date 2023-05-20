package com.example.smarthomeapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.Models.RoutineModel
import com.example.smarthomeapp.R
import com.example.smarthomeapp.SelectRoutine
import com.example.smarthomeapp.fragments.FavoritesFragment


class FavoritesAdapter(val context: Context, val items: ArrayList<RoutineModel>, val fragment: FavoritesFragment) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    /**
     * Inflates the item views which is designed in the XML layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.favorite_routine_row,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.tv_favoriteRoutineName.text = item.routineName

        holder.llMain.setOnClickListener { view ->
            fragment.deleteFromFavorites(item)
        }

    }


    //Gets the number of items in the list
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val llMain = view.findViewById<LinearLayout>(R.id.favorite_routine_row)
        val tv_favoriteRoutineName =
            view.findViewById<TextView>(R.id.tv_favoriteRoutineName)
    }
}