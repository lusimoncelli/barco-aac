package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoopActivity extends AppCompatActivity {

    private int layoutId;
    private EditText enteredText;
    private Handler handler = new Handler();
    private Handler longPressHandler = new Handler();

    // Button initialization
    private Integer[] buttonsId;
    private Button[] buttons;
    private String[] initialButtonTexts;
    private int currentButtonIndex = 0;
    private boolean loopRunning = false;
    private Button backButton;
    private boolean isLongPressing = false;

    protected LoopActivity(Integer[] buttonsId, int layoutId, String[] initialButtonTexts) {
        this.buttonsId = buttonsId;
        this.buttons = new Button[buttonsId.length];
        this.layoutId = layoutId;
        this.initialButtonTexts = initialButtonTexts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutId);

        Button backButton = findViewById(R.id.button_back_to_main);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the behavior to return to the main activity here
                Intent intent = new Intent(LoopActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        enteredText = findViewById(R.id.enteredText);
        enteredText.setTextColor(getResources().getColor(R.color.black));
        enteredText.setVisibility(View.VISIBLE);

        int index = 0;
        for (Integer buttonId : buttonsId)
            this.buttons[index++] = findViewById(buttonId);

        for (Button button : buttons) {
            button.setVisibility(View.INVISIBLE);
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        isLongPressing = true;
                        longPressHandler.postDelayed(longPressRunnable, 2000);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        isLongPressing = false;
                        longPressHandler.removeCallbacks(longPressRunnable);
                    }
                    return false;
                }
            });
        }

        setInitialButtonAsVisible();
        startLoop();
    }

    private void setButtonVisibility(int index, int visibility) {
        if (index >= 0 && index < buttons.length) {
            buttons[index].setVisibility(visibility);
        }
    }

    private void setInitialButtonAsVisible() {
        loopRunning = true;
        setButtonVisibility(currentButtonIndex, View.VISIBLE);
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
        },FrequencyHolder.getFrequency());
    }

    private void stopButtonLoop() {
        loopRunning = false;
        setButtonVisibility(currentButtonIndex, View.INVISIBLE);
    }

    private void appendText(String text) {
        enteredText.append(text);
        stopButtonLoop();
        setInitialButtonAsVisible();
    }

    public Button getButton() {
        return buttons[currentButtonIndex];
    }

    public void onButtonClick(View view) {
        if(!loopRunning) return;

        Button clickedButton = (Button) view;
        String buttonText = clickedButton.getText().toString();
        int buttonTextLength = buttonText.length();
        if( buttonTextLength == 1){
            appendText(buttonText);
            restartButtons();
        }else{
            this.buttons[0].setText(buttonText.substring(0, buttonTextLength / 2));
            this.buttons[1].setText(buttonText.substring(buttonTextLength / 2));
        }
    }

    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            enteredText.setText("");
            stopButtonLoop();
            setInitialButtonAsVisible();
        }
    };

    private void restartButtons(){
        int index = 0;
        for(String initialText: this.initialButtonTexts){
            this.buttons[index++].setText(initialText);
        }
    }

}

