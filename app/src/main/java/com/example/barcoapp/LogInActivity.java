package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    private Button[] buttons = new Button[3]; // Array to hold the buttons
    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = false; // Flag to control the loop

    private Handler handler = new Handler(); // Handler instance to manage button visibility

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
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the calibrations activity
                startActivity(new Intent(LogInActivity.this, CalibrationsActivity.class));
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the bluetooth activity
                startActivity(new Intent(LogInActivity.this, BluetoothActivity.class));
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
