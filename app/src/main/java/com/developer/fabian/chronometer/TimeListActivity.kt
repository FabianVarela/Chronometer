package com.developer.fabian.chronometer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import java.util.*

class TimeListActivity : AppCompatActivity() {

    companion object {
        private const val SECOND_KEY = "Seconds"
        private const val TIME_LIST_KEY = "TimeList"
    }

    private var listTime: ArrayList<String>? = null
    private var results: String = ""

    private lateinit var textTimeList: TextView
    private lateinit var seconds: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_list)

        setUp()
        setListFromIntent()

        if (listTime != null) {
            for (res in listTime!!)
                results += res + "\n"
        }

        textTimeList.text = results
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backIntent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                backIntent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        textTimeList = findViewById(R.id.txtListTime)
    }

    private fun setListFromIntent() {
        listTime = intent.getStringArrayListExtra(TIME_LIST_KEY)
        seconds = intent.getStringExtra(SECOND_KEY)
    }

    private fun backIntent() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putStringArrayListExtra(TIME_LIST_KEY, listTime)
        intent.putExtra(SECOND_KEY, seconds)

        startActivity(intent)
    }
}
