package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class LoopActivity extends AppCompatActivity {
    private boolean configCarrouselActivated = false;
    private final int layoutId;
    private EditText enteredText;
    private final Handler handler = new Handler();
    private final Handler longPressHandler = new Handler();
    private final Handler checkSensorDataHandler = new Handler();

    // Button initialization
    private final Integer[] buttonsId;
    private Button[] buttons;
    private Button[] configButtons;
    private final String[] initialButtonTexts;
    private int currentButtonIndex = 0;
    private boolean loopRunning = false;

    private boolean isLongPressing = false;

    protected LoopActivity(Integer[] buttonsId, int layoutId, String[] initialButtonTexts) {
        this.buttonsId = buttonsId;
        this.buttons = new Button[buttonsId.length];
        this.layoutId = layoutId;
        this.initialButtonTexts = initialButtonTexts;
    }

    private void initializeConfigCarrousel(){
        this.configButtons = new Button[3];
        Button backButton = findViewById(R.id.button_back_to_main);
        backButton.setVisibility(View.INVISIBLE);
        backButton.setOnClickListener(v -> {
            if(! isLongPressing){
                Intent intent = new Intent(LoopActivity.this, MainActivity.class);
                checkSensorDataHandler.removeCallbacksAndMessages(null);
                handler.removeCallbacksAndMessages(null);
                startActivity(intent);
            }
        });

        Button delButton=findViewById(R.id.button_delete_last);
        delButton.setVisibility(View.INVISIBLE);
        delButton.setOnClickListener(v -> {
            if(! isLongPressing){
                String currentText = enteredText.getText().toString();
                String newText = currentText.substring(0, currentText.length() - 1);
                enteredText.setText(newText);
            }
        });

        Button deleteAllButton = findViewById(R.id.delete_all);
        deleteAllButton.setVisibility(View.INVISIBLE);
        deleteAllButton.setOnClickListener(view -> {
            if(! isLongPressing)
                enteredText.setText("");
            });

        this.configButtons[0] = backButton;
        this.configButtons[1] = deleteAllButton;
        this.configButtons[2] =delButton;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutId);

        // Start variable check
        startSensorDataCheck();

        initializeConfigCarrousel();

        enteredText = findViewById(R.id.enteredText);
        enteredText.setTextColor(getResources().getColor(R.color.black));
        enteredText.setVisibility(View.VISIBLE);

        int index = 0;
        for (Integer buttonId : buttonsId)
            this.buttons[index++] = findViewById(buttonId);

        setInitialButtonAsVisible();
        startLoop();
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
        Button visibleButton;
        if(!configCarrouselActivated)
         visibleButton = buttons[currentButtonIndex];
        else visibleButton = configButtons[currentButtonIndex];
        runOnUiThread(() -> visibleButton.performClick());
    }

    private void performLongClick() {
        loopRunning = false;
        if(!configCarrouselActivated){
            for (Button button : buttons) {
                button.setVisibility(View.INVISIBLE);}}
        else {setButtonVisibility(currentButtonIndex, View.INVISIBLE);}
        currentButtonIndex = 0 ;
        configCarrouselActivated = !configCarrouselActivated;

        if(!configCarrouselActivated){
            restartButtons();}

        setInitialButtonAsVisible();
        handler.removeCallbacksAndMessages(null);
        startLoop();

    }

    private void setButtonVisibility(int index, int visibility) {
        if(!configCarrouselActivated){
            if (index >= 0 && index < buttons.length){
                buttons[index].setVisibility(visibility);
            }
        }else{
            if (index >= 0 && index < configButtons.length)
                configButtons[index].setVisibility(visibility);
        }
    }

    private void setButtonEnable(int index, boolean isEnabled) {
        if (!configCarrouselActivated) {
            if (index >= 0 && index < buttons.length) {
                buttons[index].setEnabled(isEnabled);
            }
        } else {
            if (index >= 0 && index < configButtons.length) {
                configButtons[index].setEnabled(isEnabled);
            }
        }
    }


    private void setInitialButtonAsVisible() {
        loopRunning = true;
        if(!configCarrouselActivated){
            for (Button button : buttons) {
                button.setVisibility(View.VISIBLE);}}
        else {setButtonVisibility(currentButtonIndex,View.VISIBLE);}
        }


    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!configCarrouselActivated){
                    setButtonEnable(currentButtonIndex, false);
                    currentButtonIndex = (currentButtonIndex + 1) % buttons.length;
                    setButtonEnable(currentButtonIndex, true);}

                else{
                    setButtonVisibility(currentButtonIndex,View.INVISIBLE);
                    currentButtonIndex = (currentButtonIndex + 1) % configButtons.length;
                    setButtonVisibility(currentButtonIndex,View.VISIBLE);
                }

                if (loopRunning) {
                    handler.postDelayed(this, FrequencyHolder.getFrequency());
                }
            }
        },FrequencyHolder.getFrequency());
    }

    private void stopButtonLoop() {
        loopRunning = false;
        for (Button button : buttons) {
            button.setVisibility(View.INVISIBLE);}
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
        if(!loopRunning || isLongPressing) return;

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

    private void restartButtons(){
        int index = 0;
        for(String initialText: this.initialButtonTexts){
            this.buttons[index++].setText(initialText);
        }
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

