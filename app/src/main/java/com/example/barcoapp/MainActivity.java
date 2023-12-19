package com.example.barcoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final Button[] buttons_main = new Button[2]; // Array to hold the buttons
    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = false; // Flag to control the loop
    private final Handler handler = new Handler(); // Handler instance to manage button visibility
    private final Handler checkSensorDataHandler = new Handler();
    private final String[] initialButtonTexts = {"ALFABÉTICO NÚMEROS", "PALABRAS PICTOGRAMAS INICIO"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the buttons using their IDs from the activity_calibrations layout
        buttons_main[0] = findViewById(R.id.button_abcde);
        buttons_main[1] = findViewById(R.id.button_numbers);

        startLoop(); // Start the button visibility loop
        startSensorDataCheck();
    }

    public void onButtonClick(View view) {
        Button clickedButton = (Button) view;
        String[] words = clickedButton.getText().toString().split(" ");
        if (words.length == 1) {
            if ("ALFABÉTICO".equals(words[0])) {
                Intent intent = new Intent(MainActivity.this, AlphanumericKeyboardActivity.class);
                handler.removeCallbacksAndMessages(null);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);}
            else if ("NÚMEROS".equals(words[0])){
                Intent intent = new Intent(MainActivity.this, NumbersKeyboardActivity.class);
                handler.removeCallbacksAndMessages(null);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);}
            else if ("PALABRAS".equals(words[0])){
                Intent intent = new Intent(MainActivity.this, NewKeyboardActivity.class);
                handler.removeCallbacksAndMessages(null);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);}
            else if ("PICTOGRAMAS".equals(words[0])){
                Intent intent = new Intent(MainActivity.this, PictogramActivity.class);
                handler.removeCallbacksAndMessages(null);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);}
            else if ("INICIO".equals(words[0])){
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                handler.removeCallbacksAndMessages(null);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);
            }
            restartButtons();
        }
        else{
            String firstButton = "", secondButton="";
            for(int i = 0 ; i < words.length  ; i++){
                if ( i < words.length / 2)
                    firstButton += words[i] + " ";
                else
                    secondButton += words[i] + " ";
            }
            this.buttons_main[1].setText(firstButton);
            this.buttons_main[0].setText(secondButton);
        }
    }

    protected void restartButtons(){
        int index = 0;
        for(String initialText: this.initialButtonTexts){
            this.buttons_main[index++].setText(initialText);
        }
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

        Button visibleButton = buttons_main[currentButtonIndex];
        runOnUiThread(() -> visibleButton.performClick());
    }

    private void setButtonEnable(int index, boolean isEnabled) {
        if (index >= 0 && index < buttons_main.length) {
            buttons_main[index].setEnabled(isEnabled);
        }}

    private void startLoop() {
        loopRunning = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonEnable(currentButtonIndex, true);
                currentButtonIndex = (currentButtonIndex + 1) % buttons_main.length;
                setButtonEnable(currentButtonIndex, false);

                if (loopRunning) {
                    handler.postDelayed(this, FrequencyHolder.getFrequency());
                }
            }
        }, 0); // Start the loop immediately
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