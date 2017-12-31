package com.developer.fabian.chronometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeListActivity extends AppCompatActivity {

    private final static String SECOND_KEY = "Seconds";
    private final static String TIME_LIST_KEY = "TimeList";

    private ArrayList<String> listTime;
    private TextView textTimeList;
    private String results = "";
    private String seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_list);

        setUp();
        setListFromIntent();

        if (listTime != null) {
            for (String res : listTime)
                results += res + "\n";
        }

        textTimeList.setText(results);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backIntent();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUp() {
        textTimeList = findViewById(R.id.txtListTime);
    }

    private void setListFromIntent() {
        listTime = getIntent().getStringArrayListExtra(TIME_LIST_KEY);
        seconds = getIntent().getStringExtra(SECOND_KEY);
    }

    private void backIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putStringArrayListExtra(TIME_LIST_KEY, listTime);
        intent.putExtra(SECOND_KEY, seconds);

        startActivity(intent);
    }
}
