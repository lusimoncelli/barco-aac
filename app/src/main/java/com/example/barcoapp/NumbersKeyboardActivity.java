package com.example.barcoapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NumbersKeyboardActivity extends AppCompatActivity {

    private EditText enteredText;
    private Handler handler = new Handler();

    // Button initialization
    private Button[] numberButtons;
    private Button backButton;
    private int currentButtonIndex = 0;
    private boolean loopRunning = false;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numbers_layout);

        enteredText = findViewById(R.id.enteredText);
        enteredText.setTextColor(getResources().getColor(R.color.black));
        enteredText.setVisibility(View.VISIBLE);

        // Initialize buttons
        numberButtons = new Button[]{
                findViewById(R.id.button_zero),
                findViewById(R.id.button_one),
                findViewById(R.id.button_two),
                findViewById(R.id.button_three),
                findViewById(R.id.button_four),
                findViewById(R.id.button_five),
                findViewById(R.id.button_six),
                findViewById(R.id.button_seven),
                findViewById(R.id.button_eight),
                findViewById(R.id.button_nine),

        };

        for (Button button : numberButtons) {
            button.setVisibility(View.INVISIBLE);
        }

        setButtonVisibility(currentButtonIndex, View.VISIBLE);
        startButtonLoop();

    }

    private void setButtonVisibility(int index, int visibility){
        if (index >= 0 && index < numberButtons.length){
            numberButtons[index].setVisibility(visibility);
        }
    }

    private void startButtonLoop(){
        loopRunning = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonVisibility(currentButtonIndex, View.INVISIBLE);
                currentButtonIndex = (currentButtonIndex + 1) % numberButtons.length;
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

    public Button getButton(){
        return numberButtons[currentButtonIndex];
    }

    public void onButtonClick(View view){

        if (loopRunning){
            Button clickedButton = (Button) view;
            String buttonText = clickedButton.getText().toString();
            appendText(buttonText);
        }
    }
}
