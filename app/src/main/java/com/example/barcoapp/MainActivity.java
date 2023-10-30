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
    private Button buttonSettings;
    private Button buttonABCDE;
    private Button buttonNumbers;
    private Button buttonNewKeyboard;
    // Flag to control the button loop
    private boolean buttonSequenceRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNumbers = findViewById(R.id.button_numbers);
        buttonSettings = findViewById(R.id.button_settings);
        buttonABCDE = findViewById(R.id.button_abcde);
        buttonNewKeyboard = findViewById(R.id.button_newkeyboard);

        // Set buttons as invisible
        buttonNumbers.setVisibility(View.INVISIBLE);
        buttonNewKeyboard.setVisibility(View.INVISIBLE);
        buttonABCDE.setVisibility(View.INVISIBLE);

        StartButtonAppearanceSequence();

        // Go to Settings
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSequenceRunning = false;
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        // Go to numbers keyboard
        buttonNumbers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                buttonSequenceRunning = false;
                Intent intent = new Intent(MainActivity.this, NumbersKeyboardActivity.class);
                startActivity(intent);
            }
        });

        // Go to alphanumeric keyboard
        buttonABCDE.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                buttonSequenceRunning = false;
                Intent intent = new Intent(MainActivity.this, AlphanumericKeyboardActivity.class);
                startActivity(intent);
            }
        });

        // Go to create keyboard
        buttonNewKeyboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                buttonSequenceRunning = false;
                Intent intent = new Intent(MainActivity.this, NewKeyboardActivity.class);
                startActivity(intent);
            }

});
    }

    private void StartButtonAppearanceSequence(){
        // Initial sequence
        buttonSequence();
    }

    private void buttonSequence() {
        if (!buttonSequenceRunning){
            return; // Exit when a button is clicked
        }

        // Show buttonABCDE
        buttonABCDE.setVisibility(View.VISIBLE);

        /*
        // Check for sensor data
        if (BluetoothActivity.buttonClick) {
            buttonClick();
        }

         */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide buttonABCDE and show buttonNumbers
                buttonABCDE.setVisibility(View.INVISIBLE);
                buttonNumbers.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide buttonNumbers and show buttonNewKeyboard
                        buttonNumbers.setVisibility(View.INVISIBLE);
                        buttonNewKeyboard.setVisibility(View.VISIBLE);
                        /*
                        // Check for sensor data:
                        if (BluetoothActivity.buttonClick) {
                            buttonClick();
                        }

                         */
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Hide buttonNewKeyboard and restart the sequence
                                buttonNewKeyboard.setVisibility(View.INVISIBLE);
                                buttonSequence();
                            }
                        }, 1000); // Delay before restarting the sequence
                    }
                }, 1000);
            }
        }, 1000);
    }

    // Helper method to perform the button click
    public void buttonClick(View view) {
        Button visibleButton = getVisibleButton();
        if (visibleButton != null) {
            visibleButton.performClick();
            BluetoothActivity.buttonClick = false; // Reset the buttonClick variable
        }
    }

    private Button getVisibleButton() {
        if (buttonNumbers.getVisibility() == View.VISIBLE) {
            return buttonNumbers;
        } else if (buttonNewKeyboard.getVisibility() == View.VISIBLE) {
            return buttonNewKeyboard;
        } else if (buttonABCDE.getVisibility() == View.VISIBLE){
            return buttonABCDE;
        } else {
            return null;
        }
    }

}
