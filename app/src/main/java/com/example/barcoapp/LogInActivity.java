package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    private final Button[] buttons = new Button[3]; // Array to hold the buttons
    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = false; // Flag to control the loop
    private final Handler checkSensorDataHandler = new Handler();
    private final int CHECK_INTERVAL = 50; // milliseconds
    private final Handler handler = new Handler(); // Handler instance to manage button visibility

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize the buttons using their IDs
        buttons[0] = findViewById(R.id.button_keyboards);
        buttons[1] = findViewById(R.id.button_calibrations);
        buttons[2] = findViewById(R.id.button_BT);

        // Set buttons initially invisible
        setButtonVisibility(0, View.INVISIBLE);
        setButtonVisibility(1, View.INVISIBLE);
        setButtonVisibility(2, View.INVISIBLE);

        startButtonLoop(); // Start the button visibility loop

        // Set click listeners for each button
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the main activity
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the calibrations activity
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(LogInActivity.this, CalibrationsActivity.class);
                startActivity(intent);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the bluetooth activity
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(LogInActivity.this, BluetoothActivity.class);
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

                checkSensorDataHandler.postDelayed(this, CHECK_INTERVAL);
            }
        }, CHECK_INTERVAL);
    }

    private void pressVisibleButton() {

        Button visibleButton = buttons[currentButtonIndex];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visibleButton.performClick();
            }
        });
    }

    private void performLongClick() {
        Button visibleButton = buttons[currentButtonIndex];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visibleButton.performLongClick();
            }
        });
    }

    private void setButtonVisibility(int index, int visibility) {
        if (index >= 0 && index < buttons.length) {
            buttons[index].setVisibility(visibility);
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
                currentButtonIndex = (currentButtonIndex + 1) % buttons.length;
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


}
