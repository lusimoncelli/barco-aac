package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    // Buttons Initialization
    private Button[] buttons = new Button[4];

    // Flag to control the button loop
    private boolean loopRunning = false;
    private int currentButtonIndex = 0; // Current index for the button visibility loop

    private Handler checkSensorDataHandler = new Handler();
    private int CHECK_INTERVAL = 50; // milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons[0] = findViewById(R.id.button_numbers);
        buttons[1] = findViewById(R.id.button_settings);
        buttons[2] = findViewById(R.id.button_abcde);
        buttons[3] = findViewById(R.id.button_newkeyboard);

        // Set buttons as invisible
        buttons[0].setVisibility(View.INVISIBLE);
        buttons[1].setVisibility(View.INVISIBLE);
        buttons[2].setVisibility(View.INVISIBLE);
        buttons[3].setVisibility(View.INVISIBLE);

        startButtonLoop();

        // Go to Settings
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopRunning = false;
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        // Go to numbers keyboard
        buttons[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loopRunning = false;
                Intent intent = new Intent(MainActivity.this, NumbersKeyboardActivity.class);
                startActivity(intent);
            }
        });

        // Go to alphanumeric keyboard
        buttons[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loopRunning = false;
                Intent intent = new Intent(MainActivity.this, AlphanumericKeyboardActivity.class);
                startActivity(intent);
            }
        });

        // Go to create keyboard
        buttons[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loopRunning = false;
                Intent intent = new Intent(MainActivity.this, NewKeyboardActivity.class);
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