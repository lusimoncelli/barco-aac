package com.example.barcoapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CalibrationsActivity extends AppCompatActivity {

    private Button[] buttons = new Button[3]; // Array to hold the buttons
    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = false; // Flag to control the loop
    private int frequency = 200; // Default frequency

    private Handler handler = new Handler(); // Handler instance to manage button visibility

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrations);

        // Initialize the buttons using their IDs from the activity_calibrations layout
        buttons[0] = findViewById(R.id.button_slow);
        buttons[1] = findViewById(R.id.button_medium);
        buttons[2] = findViewById(R.id.button_high);

        // Set buttons initially invisible
        setButtonVisibility(0, View.INVISIBLE);
        setButtonVisibility(1, View.INVISIBLE);
        setButtonVisibility(2, View.INVISIBLE);

        startButtonLoop(); // Start the button visibility loop

        // Set click listeners for the slow, medium, and high buttons
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frequency = 1000; // Set frequency to 1000
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frequency = 1500; // Set frequency to 1500
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frequency = 2000; // Set frequency to 2000
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
                    handler.postDelayed(this, frequency);
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
