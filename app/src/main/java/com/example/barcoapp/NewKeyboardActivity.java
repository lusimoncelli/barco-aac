package com.example.barcoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NewKeyboardActivity extends AppCompatActivity {

    private final Button[] buttons_shortcuts = new Button[6]; // Array to hold the buttons
    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = false; // Flag to control the loop
    private final Handler handler = new Handler(); // Handler instance to manage button visibility
    private EditText enteredText;
    private final Handler checkSensorDataHandler = new Handler();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_keyboard_layout);

        // Initialize the buttons using their IDs from the activity_calibrations layout
        buttons_shortcuts[0] = findViewById(R.id.button_hola);
        buttons_shortcuts[1] = findViewById(R.id.button_chau);
        buttons_shortcuts[2] = findViewById(R.id.button_si);
        buttons_shortcuts[3] = findViewById(R.id.button_no);
        buttons_shortcuts[4] = findViewById(R.id.button_back_to_main);
        buttons_shortcuts[5] = findViewById(R.id.button_delete_all);

        // Set buttons initially invisible
        setButtonVisibility(0, View.INVISIBLE);
        setButtonVisibility(1, View.INVISIBLE);
        setButtonVisibility(2, View.INVISIBLE);
        setButtonVisibility(3, View.INVISIBLE);
        setButtonVisibility(4,View.INVISIBLE);
        setButtonVisibility(5,View.INVISIBLE);

        enteredText = findViewById(R.id.enteredText);
        enteredText.setTextColor(getResources().getColor(R.color.black));
        enteredText.setVisibility(View.VISIBLE);

        startButtonLoop();

        buttons_shortcuts[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(view);
            }
        });

        buttons_shortcuts[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(view);
            }
        });

        buttons_shortcuts[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(view);
            }
        });

        buttons_shortcuts[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(view);
            }
        });

        buttons_shortcuts[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean buttonSequenceRunning = false;
                Intent intent = new Intent(NewKeyboardActivity.this, LogInActivity.class);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                startActivity(intent);
            }
        });

        buttons_shortcuts[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredText.setText("");
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

        Button visibleButton = buttons_shortcuts[currentButtonIndex];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visibleButton.performClick();
            }
        });
    }

    private void performLongClick() {
        Button visibleButton = buttons_shortcuts[currentButtonIndex];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visibleButton.performLongClick();
            }
        });
    }

    public void onButtonClick(View view) {
        Button clickedButton = (Button) view;
        String buttonText = clickedButton.getText().toString();
        setEnteredText(buttonText);
    }

    private void setEnteredText(String text) {
        enteredText.setText(text);
    }


    private void setButtonVisibility(int index, int visibility) {
        if (index >= 0 && index < buttons_shortcuts.length) {
            buttons_shortcuts[index].setVisibility(visibility);
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
                currentButtonIndex = (currentButtonIndex + 1) % buttons_shortcuts.length;
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




