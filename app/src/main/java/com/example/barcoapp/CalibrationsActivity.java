package com.example.barcoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalibrationsActivity extends AppCompatActivity {

    private final Button[] buttons_calibrations = new Button[4]; // Array to hold the buttons

    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = false; // Flag to control the loop
    private final Handler handler = new Handler(); // Handler instance to manage button visibility
    private final Handler checkSensorDataHandler = new Handler();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrations);

        // Initialize the buttons using their IDs from the activity_calibrations layout
        buttons_calibrations[0] = findViewById(R.id.button_slow);
        buttons_calibrations[1] = findViewById(R.id.button_medium);
        buttons_calibrations[2] = findViewById(R.id.button_high);
        buttons_calibrations[3] = findViewById(R.id.button_settings);



        // Set buttons initially invisible
        setButtonVisibility(0, View.INVISIBLE);
        setButtonVisibility(1, View.INVISIBLE);
        setButtonVisibility(2, View.INVISIBLE);
        setButtonVisibility(3, View.INVISIBLE);

        startButtonLoop(); // Start the button visibility loop

        // Set click listeners for the slow, medium, and high buttons
        buttons_calibrations[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrequencyHolder.setFrequency(3000); // Set frequency to 750

                // Display a pop-up notification
                Toast.makeText(getApplicationContext(), "La velocidad de rotación fue modificada a: BAJA", Toast.LENGTH_SHORT).show();
            }
        });

        buttons_calibrations[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrequencyHolder.setFrequency(2000); // Set frequency to 2000

                // Display a pop-up notification
                Toast.makeText(getApplicationContext(), "La velocidad de rotación fue modificada a: MEDIA", Toast.LENGTH_SHORT).show();
            }
        });


        buttons_calibrations[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrequencyHolder.setFrequency(1000); // Set frequency to 250
                // Display a pop-up notification
                Toast.makeText(getApplicationContext(), "La velocidad de rotación fue modificada a: ALTA", Toast.LENGTH_SHORT).show();
            }
        });

        buttons_calibrations[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean buttonSequenceRunning = false;
                Intent intent = new Intent(CalibrationsActivity.this, LogInActivity.class);
                handler.removeCallbacksAndMessages(null);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);
            }
        });

        // Access sensorDataApplication to retrieve sensor data
        SensorDataApplication sensorDataApplication = (SensorDataApplication) getApplication();
        // Start variable check
        startSensorDataCheck();
    }

    private void startSensorDataCheck() {
        checkSensorDataHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String receivedData = SensorDataApplication.getSensorData();
                if ("0".equals(receivedData)) {
                    pressVisibleButton();
                } else if ("2".equals(receivedData)) {
                    performLongClick();
                }

                checkSensorDataHandler.postDelayed(this, Constants.CHECK_INTERVAL);
            }
        }, Constants.CHECK_INTERVAL);
    }

    private void pressVisibleButton() {

        Button visibleButton = buttons_calibrations[currentButtonIndex];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visibleButton.performClick();
            }
        });
    }

    private void performLongClick() {
        Button visibleButton = buttons_calibrations[currentButtonIndex];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visibleButton.performLongClick();
            }
        });
    }

    private void setButtonVisibility(int index, int visibility) {
        if (index >= 0 && index < buttons_calibrations.length) {
            buttons_calibrations[index].setVisibility(visibility);
        }
    }

    private void startButtonLoop() {
        loopRunning = true;
        startLoop();
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonVisibility(currentButtonIndex, View.INVISIBLE);
                currentButtonIndex = (currentButtonIndex + 1) % buttons_calibrations.length;
                setButtonVisibility(currentButtonIndex, View.VISIBLE);

                if (loopRunning) {
                    handler.postDelayed(this, FrequencyHolder.getFrequency());
                }
            }
        }, 0); // Start the loop immediately
    }

    private void stopButtonLoop() {
        loopRunning = false;
        setButtonVisibility(currentButtonIndex, View.INVISIBLE);
        handler.removeCallbacksAndMessages(null); // Remove any pending posts
    }

    @Override
    protected void onPause(){
        super.onPause();
        checkSensorDataHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensorDataCheck();
    }
}