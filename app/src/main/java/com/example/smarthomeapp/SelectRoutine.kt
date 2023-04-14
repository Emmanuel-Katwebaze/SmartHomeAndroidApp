package com.example.smarthomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SelectRoutine : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_routine)

        val toolbar = findViewById<Toolbar>(R.id.selectRoutineToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val createRoutineButton  = findViewById<FloatingActionButton>(R.id.CreateRoutineFAB)

        createRoutineButton.setOnClickListener { createRoutine() }
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
}