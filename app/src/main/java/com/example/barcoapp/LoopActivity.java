package com.example.barcoapp;

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
    private boolean isLongPressing = false;

    // Button initialization
    private Integer[] buttonsId;

    private Button[] buttons;
    private Button backButton;
    private int currentButtonIndex = 0;
    private boolean loopRunning = false;

    protected LoopActivity(Integer[] buttonsId, int layoutId) {
        this.buttonsId = buttonsId;
        this.buttons = new Button[buttonsId.length];
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutId);

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

        startButtonLoop();
        startLoop();
    }

    private void setButtonVisibility(int index, int visibility) {
        if (index >= 0 && index < buttons.length) {
            buttons[index].setVisibility(visibility);
        }
    }

    private void startButtonLoop() {
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
        }, 1000);
    }

    private void stopButtonLoop() {
        loopRunning = false;
        setButtonVisibility(currentButtonIndex, View.INVISIBLE);
    }

    private void appendText(String text) {
        enteredText.append(text);
        stopButtonLoop();
        startButtonLoop();
    }

    public Button getButton() {
        return buttons[currentButtonIndex];
    }

    public void onButtonClick(View view) {
        if (loopRunning) {
            Button clickedButton = (Button) view;
            String buttonText = clickedButton.getText().toString();
            appendText(buttonText);
        }
    }

    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            enteredText.setText("");
            stopButtonLoop();
            startButtonLoop();
        }
    };
}
