package com.developer.fabian.chronometer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static String SECOND_KEY = "Seconds";
    private final static String TIME_LIST_KEY = "TimeList";

    private int secondTime = 0;
    private boolean isRunning = false;
    private ArrayList<String> timeList;

    private TextView txtChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtChronometer = findViewById(R.id.txtChrono);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        runChronometer();
    }

    @Override
    protected void onStart() {
        super.onStart();

        verifyInitTime();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isRunning)
            isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        verifyInitTime();
        verifyIntent();
        restartChronometer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isRunning)
            isRunning = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (!isRunning)
            isRunning = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.history_main)
            clearTimes();

        return super.onOptionsItemSelected(item);
    }

    public void onClickStart(View view) {
        isRunning = true;
    }

    public void onClickStop(View view) {
        isRunning = false;
        saveTimes();
    }

    public void onClickRestart(View view) {
        secondTime = 0;
        isRunning = false;
    }

    public void onActivityListChronometer(View view) {
        Intent listIntent = new Intent(this, TimeListActivity.class);
        listIntent.putExtra(SECOND_KEY, String.valueOf(secondTime));
        listIntent.putStringArrayListExtra(TIME_LIST_KEY, timeList);

        startActivity(listIntent);
    }

    private void runChronometer() {
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = secondTime / 3600;
                int minutes = (secondTime % 3600) / 60;
                int seconds = secondTime % 60;

                txtChronometer.setText(getString(R.string.format_chronometer, hours, minutes, seconds));

                if (isRunning)
                    secondTime++;

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void verifyInitTime() {
        if (!txtChronometer.getText().equals(getString(R.string.time)) && !isRunning)
            isRunning = true;
    }

    private void verifyIntent() {
        if (getIntent().getStringArrayListExtra(TIME_LIST_KEY) != null && timeList == null)
            timeList = getIntent().getStringArrayListExtra(TIME_LIST_KEY);

        if (getIntent().getStringExtra(SECOND_KEY) != null && secondTime == 0)
            secondTime = Integer.parseInt(getIntent().getStringExtra(SECOND_KEY));
    }

    private void restartChronometer() {
        if (secondTime > 0) {
            int hours = 0;
            int minutes = 0;
            int seconds = 0;

            for (int i = 0; i <= secondTime; i++) {
                hours = secondTime / 3600;
                minutes = (secondTime % 3600) / 60;
                seconds = secondTime % 60;
            }

            txtChronometer.setText(getString(R.string.format_chronometer, hours, minutes, seconds));
            isRunning = true;
        }
    }

    private void clearTimes() {
        timeList.clear();
        Toast.makeText(this, R.string.messageDelete, Toast.LENGTH_SHORT).show();
    }

    private void saveTimes() {
        if (timeList == null)
            timeList = new ArrayList<>();

        if (timeList.size() < 5)
            timeList.add(txtChronometer.getText().toString());
        else
            Toast.makeText(this, R.string.messageList, Toast.LENGTH_SHORT).show();
    }
}
