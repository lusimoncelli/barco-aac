package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;

public class LoopActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private boolean configCarrouselActivated = false;
    private final int layoutId;
    private EditText enteredText;

    // Button initialization
    private final Integer[] buttonsId;
    protected Button[] buttons;
    private Button[] configButtons;
    private final String[] initialButtonTexts;
    private int currentButtonIndex = 0;
    protected boolean loopRunning = false;
    protected boolean isLongPressing = false;
    private final Handler mainHandler = new Handler(msg -> {
        switch (msg.what) {
            case Constants.CHECK_SENSOR_DATA:
                handleSensorData();
                return true;
            case Constants.BUTTON_LOOP:
                handleButtonLoop();
                return true;
            default:
                return false;
        }
    });

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

        // Initialize components
        initializeConfigCarrousel();
        initializeText();
        textToSpeechInitialization();
        initializeButtons();
        setInitialButtonAsVisible();

        // Initialize sensor data check
        startSensorDataCheck();

        handleButtonLoop();
    }

    private void startSensorDataCheck(){
        mainHandler.sendEmptyMessageDelayed(Constants.CHECK_SENSOR_DATA, Constants.CHECK_INTERVAL);
    }

    private void initializeConfigCarrousel(){
        this.configButtons = new Button[4];

        Button backButton = findViewById(R.id.button_back_to_main);
        backButton.setVisibility(View.INVISIBLE);
        backButton.setOnClickListener(v -> {
            if(! isLongPressing){
                Intent intent = new Intent(LoopActivity.this, MainActivity.class);
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


        Button ReadAloud = findViewById(R.id.button_read_out_loud);
        ReadAloud.setVisibility(View.INVISIBLE);

        this.configButtons[0] = ReadAloud;
        this.configButtons[1] = deleteAllButton;
        this.configButtons[2] = delButton;
        this.configButtons[3] = backButton;


    }


    private void initializeText() {

        enteredText = findViewById(R.id.enteredText);
        enteredText.setTextColor(getResources().getColor(R.color.black));
        enteredText.setVisibility(View.VISIBLE);
    }

    private void textToSpeechInitialization() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("es", "ES")); // Spanish (Spain)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    showToast("Language is not supported.");
                }
            } else {
                showToast("TextToSpeech initialization failed.");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(LoopActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void initializeButtons() {
        int index = 0;
        for (Integer buttonId : buttonsId) {
            buttons[index] = findViewById(buttonId);
            setButtonEnable(index, loopRunning); // Set the initial state
            index++;
        }
    }


    public void onReadButtonClick(View view) {
        // Get the text entered in the EditText
        String textToRead = enteredText.getText().toString().trim();

        // Check if TextToSpeech is initialized and the specified text is not empty
        if (textToSpeech != null && !textToRead.isEmpty()) {
            // Set up a HashMap to specify the utterance ID
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "readAloud");

            // Speak the entered text
            textToSpeech.speak(textToRead, TextToSpeech.QUEUE_FLUSH, params);
        }}

    private void handleSensorData() {
        String receivedData = SensorDataApplication.getSensorData();
        if ("0".equals(receivedData)) {
            pressVisibleButton();
        } else if ("2".equals(receivedData)) {
            performLongClick();
        }
        startSensorDataCheck(); // Reschedule the check
    }


    private void handleButtonLoop() {
        runOnUiThread(() -> {
            if (!configCarrouselActivated) {
                setButtonEnable(currentButtonIndex, false);
                currentButtonIndex = (currentButtonIndex + 1) % buttons.length;
                setButtonEnable(currentButtonIndex, true);
            } else {
                setButtonVisibility(currentButtonIndex, View.INVISIBLE);
                currentButtonIndex = (currentButtonIndex + 1) % configButtons.length;
                setButtonVisibility(currentButtonIndex, View.VISIBLE);
            }

            if (loopRunning && !mainHandler.hasMessages(Constants.BUTTON_LOOP)) {
                mainHandler.sendEmptyMessageDelayed(Constants.BUTTON_LOOP, FrequencyHolder.getFrequency());
            }
        });
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

        if (!configCarrouselActivated) {
            for (Button button : buttons) {
                button.setVisibility(View.INVISIBLE);
            }
        } else {
            setButtonVisibility(currentButtonIndex, View.INVISIBLE);
        }

        currentButtonIndex = 0;
        configCarrouselActivated = !configCarrouselActivated;

        if (!configCarrouselActivated && !mainHandler.hasMessages(Constants.BUTTON_LOOP)) {
            restartButtons();
            handleButtonLoop();  // Start the loop only if not in config mode
        }

        setInitialButtonAsVisible();
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
                button.setVisibility(View.VISIBLE);
            }
        }
        else {setButtonVisibility(currentButtonIndex,View.VISIBLE);}
        }



    private void stopButtonLoop() {
        loopRunning = false;
        for (Button button : buttons) {
            button.setVisibility(View.INVISIBLE);}
    }

    protected void appendText(String text) {
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


    protected void restartButtons(){
        int index = 0;
        for(String initialText: this.initialButtonTexts){
            this.buttons[index++].setText(initialText);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensorDataCheck();
        handleButtonLoop();
    }

}

