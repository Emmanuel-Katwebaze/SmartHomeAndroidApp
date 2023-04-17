package com.example.smarthomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class SelectEvent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_event)

        val toolbar = findViewById<Toolbar>(R.id.selectEventToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }


        val selectEventTimeTV = findViewById<TextView>(R.id.selectEventTimeTV)

        selectEventTimeTV.setOnClickListener { setTime() }

    }

    private fun setTime() {
        val intent = Intent(this, CreateRoutine::class.java)
        intent.putExtra("timeSet", true)
        startActivity(intent)
    }
}