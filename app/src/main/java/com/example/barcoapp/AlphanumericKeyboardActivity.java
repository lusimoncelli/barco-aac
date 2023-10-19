package com.example.barcoapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlphanumericKeyboardActivity extends AppCompatActivity {
    private TextView enteredText;
    private Handler handler = new Handler();

    // Button initialization
    private Button[] letterButtons;
    private Button backButton;
    private int currentButtonIndex = 0;
    private boolean loopRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphanumeric_layout);

        enteredText = findViewById(R.id.enteredText);

        // Initialize buttons
        letterButtons = new Button[]{
                findViewById(R.id.button_A),
                findViewById(R.id.button_B),
                findViewById(R.id.button_C),
                findViewById(R.id.button_D),
                findViewById(R.id.button_E),
                findViewById(R.id.button_F),
                findViewById(R.id.button_G),
                findViewById(R.id.button_H),
                findViewById(R.id.button_I),
                findViewById(R.id.button_J),
                findViewById(R.id.button_K),
                findViewById(R.id.button_L),
                findViewById(R.id.button_M),
                findViewById(R.id.button_N),
                findViewById(R.id.button_O),
                findViewById(R.id.button_P),
                findViewById(R.id.button_Q),
                findViewById(R.id.button_R),
                findViewById(R.id.button_S),
                findViewById(R.id.button_T),
                findViewById(R.id.button_U),
                findViewById(R.id.button_V),
                findViewById(R.id.button_W),
                findViewById(R.id.button_X),
                findViewById(R.id.button_Y),
                findViewById(R.id.button_Z),

        };

        for (Button button : letterButtons) {
            button.setVisibility(View.INVISIBLE);
        }

        setButtonVisibility(currentButtonIndex, View.VISIBLE);
        startButtonLoop();


    }


    private void setButtonVisibility(int index, int visibility){
        if (index >= 0 && index < letterButtons.length){
            letterButtons[index].setVisibility(visibility);
        }
    };

    private void startButtonLoop(){
        loopRunning = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonVisibility(currentButtonIndex, View.INVISIBLE);
                currentButtonIndex = (currentButtonIndex + 1) % letterButtons.length;
                setButtonVisibility(currentButtonIndex, View.VISIBLE);

                if (loopRunning){
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void stopButtonLoop() {
        loopRunning = false;
        currentButtonIndex = 0;
    }

    private void appendText(String text) {
        enteredText.append(text);
        stopButtonLoop();
        startButtonLoop();
    }

    public void onButtonClick(View view){

        if (loopRunning){
            Button clickedButton = (Button) view;
            String buttonText = clickedButton.getText().toString();
            appendText(buttonText);
        }
    }
}