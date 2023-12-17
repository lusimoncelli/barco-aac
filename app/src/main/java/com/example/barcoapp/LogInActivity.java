package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    private final Button[] buttons = new Button[2]; // Array to hold the buttons
    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = false; // Flag to control the loop
    private final Handler checkSensorDataHandler = new Handler();
    private final Handler handler = new Handler(); // Handler instance to manage button visibility

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize the buttons using their IDs
        buttons[0] = findViewById(R.id.button_keyboards);
        buttons[1] = findViewById(R.id.button_calibrations);


        startButtonLoop(); // Start the button visibility loop

        // Set click listeners for each button
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the main activity
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the calibrations activity
                Intent intent = new Intent(LogInActivity.this, CalibrationsActivity.class);
                handler.removeCallbacksAndMessages(null);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);
            }
        });

        startSensorDataCheck();
    }

    private void startSensorDataCheck() {
        checkSensorDataHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String receivedData = SensorDataApplication.getSensorData();
                if ("0".equals(receivedData)) {
                    pressVisibleButton();
                }

                checkSensorDataHandler.postDelayed(this, Constants.CHECK_INTERVAL);
            }
        }, Constants.CHECK_INTERVAL);
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


    private void setButtonVisibility(int index, int visibility) {
        if (index >= 0 && index < buttons.length) {
            buttons[index].setVisibility(visibility);
        }
    }

    private void startButtonLoop() {
        loopRunning = true;
        startLoop();
    }

    private void setButtonEnable(int index, boolean isEnabled) {
        if (index >= 0 && index < buttons.length) {
            buttons[index].setEnabled(isEnabled);
            }
        }
    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonEnable(currentButtonIndex, false);
                currentButtonIndex = (currentButtonIndex + 1) % buttons.length;
                setButtonEnable(currentButtonIndex, true);

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
