package com.developer.fabian.chronometer

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SECOND_KEY = "Seconds"
        private const val TIME_LIST_KEY = "TimeList"
    }

    private var secondTime = 0
    private var isRunning = false
    private var timeList: ArrayList<String>? = null

    private var txtChronometer: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtChronometer = findViewById(R.id.txtChrono)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        runChronometer()
    }

    override fun onStart() {
        super.onStart()
        verifyInitTime()
    }

    override fun onPause() {
        super.onPause()

        if (isRunning)
            isRunning = false
    }

    override fun onResume() {
        super.onResume()

        verifyInitTime()
        verifyIntent()
        restartChronometer()
    }

    override fun onStop() {
        super.onStop()

        if (isRunning)
            isRunning = false
    }

    override fun onRestart() {
        super.onRestart()

        if (!isRunning)
            isRunning = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.history_main)
            clearTimes()

        return super.onOptionsItemSelected(item)
    }

    fun onClickStart(view: View) {
        isRunning = true
    }

    fun onClickStop(view: View) {
        isRunning = false
        saveTimes()
    }

    fun onClickRestart(view: View) {
        secondTime = 0
        isRunning = false
    }

    fun onActivityListChronometer(view: View) {
        val listIntent = Intent(this, TimeListActivity::class.java)
        listIntent.putExtra(SECOND_KEY, secondTime.toString())
        listIntent.putStringArrayListExtra(TIME_LIST_KEY, timeList)

        startActivity(listIntent)
    }

    private fun runChronometer() {
        val handler = Handler()

        handler.post(object : Runnable {
            override fun run() {
                val hours = secondTime / 3600
                val minutes = secondTime % 3600 / 60
                val seconds = secondTime % 60

                txtChronometer!!.text = getString(R.string.format_chronometer, hours, minutes, seconds)

                if (isRunning)
                    secondTime++

                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun verifyInitTime() {
        if (txtChronometer!!.text != getString(R.string.time) && !isRunning)
            isRunning = true
    }

    private fun verifyIntent() {
        if (intent.getStringArrayListExtra(TIME_LIST_KEY) != null && timeList == null)
            timeList = intent.getStringArrayListExtra(TIME_LIST_KEY)

        if (intent.getStringExtra(SECOND_KEY) != null && secondTime == 0)
            secondTime = Integer.parseInt(intent.getStringExtra(SECOND_KEY))
    }

    private fun restartChronometer() {
        if (secondTime > 0) {
            var hours = 0
            var minutes = 0
            var seconds = 0

            for (i in 0..secondTime) {
                hours = secondTime / 3600
                minutes = secondTime % 3600 / 60
                seconds = secondTime % 60
            }

            txtChronometer!!.text = getString(R.string.format_chronometer, hours, minutes, seconds)
            isRunning = true
        }
    }

    private fun clearTimes() {
        timeList!!.clear()
        Toast.makeText(this, R.string.messageDelete, Toast.LENGTH_SHORT).show()
    }

    private fun saveTimes() {
        if (timeList == null)
            timeList = ArrayList()

        if (timeList!!.size < 5)
            timeList!!.add(txtChronometer!!.text.toString())
        else
            Toast.makeText(this, R.string.messageList, Toast.LENGTH_SHORT).show()
    }
}
