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

    private final Button[] buttons_calibrations = new Button[2]; // Array to hold the buttons

    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = true; // Flag to control the loop
    private final Handler mainHandler = new Handler(msg -> {
        switch (msg.what) {
            case Constants.CHECK_SENSOR_DATA:
                handleSensorData();
                return true;
            case Constants.BUTTON_LOOP:
                handleButtonLoop();
                return true;
            default:
                return false;
        }
    });
    private final String[] initialButtonTexts = {"BAJA MEDIA", "ALTA INICIO"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrations);

        // Initialize the buttons using their IDs from the activity_calibrations layout
        buttons_calibrations[0] = findViewById(R.id.button_slow);
        buttons_calibrations[1] = findViewById(R.id.button_medium);

        handleButtonLoop(); // Start the button visibility loop
        startSensorDataCheck();
    }


    public void onButtonClick(View view) {
        Button clickedButton = (Button) view;
        String[] words = clickedButton.getText().toString().split(" ");
        if (words.length == 1) {
            if ("BAJA".equals(words[0])) {
                FrequencyHolder.setFrequency(3000);
                Toast.makeText(getApplicationContext(), "La velocidad de rotación fue modificada a: BAJA", Toast.LENGTH_SHORT).show();
            }
            else if ("MEDIA".equals(words[0])){
                FrequencyHolder.setFrequency(2000); // Set frequency to medium
                Toast.makeText(getApplicationContext(), "La velocidad de rotación fue modificada a: MEDIA", Toast.LENGTH_SHORT).show();
            }
            else if ("ALTA".equals(words[0])){
                FrequencyHolder.setFrequency(1000); // Set frequency to high
                Toast.makeText(getApplicationContext(), "La velocidad de rotación fue modificada a: ALTA", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(CalibrationsActivity.this, LogInActivity.class);
                mainHandler.removeCallbacksAndMessages(null);
                startActivity(intent);
            }
            restartButtons();
        }
        else{
            String firstButton = "", secondButton="";
            for(int i = 0 ; i < words.length  ; i++){
                if ( i < words.length / 2)
                    firstButton += words[i];
                else
                    secondButton += words[i];
            }
            this.buttons_calibrations[0].setText(firstButton);
            this.buttons_calibrations[1].setText(secondButton);
        }
    }

    protected void restartButtons(){
        int index = 0;
        for(String initialText: this.initialButtonTexts){
            this.buttons_calibrations[index++].setText(initialText);
        }
    }

    private void startSensorDataCheck(){
        mainHandler.sendEmptyMessageDelayed(Constants.CHECK_SENSOR_DATA, Constants.CHECK_INTERVAL);
    }

    private void handleSensorData() {
        String receivedData = SensorDataApplication.getSensorData();
        if ("0".equals(receivedData)) {
            pressVisibleButton();
        }
        startSensorDataCheck(); // Reschedule the check
    }

    private void pressVisibleButton() {

        Button visibleButton = buttons_calibrations[currentButtonIndex];
        runOnUiThread(() -> visibleButton.performClick());
    }

    private void setButtonEnable(int index, boolean isEnabled) {
            if (index >= 0 && index < buttons_calibrations.length) {
                buttons_calibrations[index].setEnabled(isEnabled);
            }}

    private void handleButtonLoop() {
        runOnUiThread(() -> {
            setButtonEnable(currentButtonIndex, false);
            currentButtonIndex = (currentButtonIndex + 1) % buttons_calibrations.length;
            setButtonEnable(currentButtonIndex, true);
            if (loopRunning && !mainHandler.hasMessages(Constants.BUTTON_LOOP)) {
                mainHandler.sendEmptyMessageDelayed(Constants.BUTTON_LOOP, FrequencyHolder.getFrequency());
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensorDataCheck();
    }
}