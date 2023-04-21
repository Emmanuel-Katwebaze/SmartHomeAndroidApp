package com.example.smarthomeapp

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar

class SelectEvent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_event)

        val arrowBack: AppCompatImageView = findViewById(R.id.arrow_back)
        arrowBack.setOnClickListener {
            finish()
        }


        val selectEventTimeTV = findViewById<TextView>(R.id.selectEventTimeTV)

        selectEventTimeTV.setOnClickListener { setTime() }


        val textViewList = listOf<TextView>(
            findViewById(R.id.selectEventTimeTV),
            findViewById(R.id.selectEventTimeTV2),
            findViewById(R.id.selectEventTimeTV3),
            findViewById(R.id.selectEventTimeTV4),
            findViewById(R.id.selectEventTimeTV5),
            findViewById(R.id.selectEventTimeTV6),
            findViewById(R.id.selectEventTimeTV7)
        )

        for (textView in textViewList) {
            formatTextView(textView)
        }





    }

    private fun setTime() {
        val intent = Intent(this, CreateRoutine::class.java)
        intent.putExtra("timeSet", true)
        startActivity(intent)
    }



    fun formatTextView(textView: TextView) {
        val text = textView.text.toString()

        if (text.contains("Time")) {
            val spannableString = SpannableString(text)
            val startIndex = text.indexOf("Time")
            val endIndex = startIndex + 4 // length of "Time"
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannableString
        } else if (text.contains("15") && text.contains("Location")) {
            val spannableString = SpannableString(text)
            val startIndex1 = text.indexOf("15")
            val endIndex1 = startIndex1 + 2 // length of "15"
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex1,
                endIndex1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val startIndex2 = text.indexOf("Location")
            val endIndex2 = startIndex2 + 8 // length of "Location"
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex2,
                endIndex2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannableString
        } else if (text.contains("Location")) {
            val spannableString = SpannableString(text)
            val startIndex = text.indexOf("Location")
            val endIndex = startIndex + 8 // length of "Location"
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannableString
        }
    }


}
