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

    private Button[] buttons_predeterminados = new Button[4]; // Array to hold the buttons
    private Button button_Home;
    private int currentButtonIndex = 0; // Current index for the button visibility loop
    private boolean loopRunning = false; // Flag to control the loop
    private Handler handler = new Handler(); // Handler instance to manage button visibility
    private EditText enteredText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_keyboard_layout);

        // Initialize the buttons using their IDs from the activity_calibrations layout
        buttons_predeterminados[0] = findViewById(R.id.button_hola);
        buttons_predeterminados[1] = findViewById(R.id.button_chau);
        buttons_predeterminados[2] = findViewById(R.id.button_si);
        buttons_predeterminados[3] = findViewById(R.id.button_no);
        button_Home = findViewById(R.id.button_Home);

        // Set buttons initially invisible
        setButtonVisibility(0, View.INVISIBLE);
        setButtonVisibility(1, View.INVISIBLE);
        setButtonVisibility(2, View.INVISIBLE);
        setButtonVisibility(3, View.INVISIBLE);
        button_Home.setVisibility(View.INVISIBLE);

        enteredText = findViewById(R.id.enteredText);
        enteredText.setTextColor(getResources().getColor(R.color.black));
        enteredText.setVisibility(View.VISIBLE);

        // Set click listeners for the buttons
        for (Button button : buttons_predeterminados) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClick(view);
                }
            });
        }

        button_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean buttonSequenceRunning = false;
                Intent intent = new Intent(NewKeyboardActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        startButtonLoop(); // Start the button visibility loop
    }

    public void onButtonClick(View view) {
        Button clickedButton = (Button) view;
        String buttonText = clickedButton.getText().toString();
        appendText(buttonText);
        // Add your other button click logic here
    }

    private void appendText(String text) {
        enteredText.append(text);
                //setInitialButtonAsVisible();
    }

    private void setButtonVisibility(int index, int visibility) {
        if (index >= 0 && index < buttons_predeterminados.length) {
            buttons_predeterminados[index].setVisibility(visibility);
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
                currentButtonIndex = (currentButtonIndex + 1) % buttons_predeterminados.length;
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
