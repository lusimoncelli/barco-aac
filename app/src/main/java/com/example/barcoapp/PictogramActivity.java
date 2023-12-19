package com.example.barcoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;

public class PictogramActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;

    private EditText enteredText;

    // Button initialization
    private Button[] configButtons;
    private final ImageButton[] imgButtons = new ImageButton[2];
    private final ImageButton[] imgButtons_when = new ImageButton[2];
    private final ImageButton[] imgButtons_drink = new ImageButton[2];
    private final String[] initialButtonTexts2 = new String[] {"BORRAR LEER", "INICIO REINICIAR"};
    private int currentButtonIndex = 0;
    // Booleans
    private boolean configCarrouselActivated = false;
    protected boolean loopRunning = false;
    protected boolean secondary_when = false;
    protected boolean secondary_drink = false;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictogram_layout);

        initializeText();
        initializeConfigCarrousel();
        initializeText();
        textToSpeechInitialization();
        initializeButtons();
        setInitialButtonAsVisible();

        // Initialize sensor data check
        startSensorDataCheck();

        handleButtonLoop();
    }

    private void initializeButtons(){
        imgButtons[0] = findViewById(R.id.button_A);
        imgButtons[1] = findViewById(R.id.button_B);

        // Buttons when and what
        imgButtons_when[0] = findViewById(R.id.button_A1);
        imgButtons_when[1] = findViewById(R.id.button_B1);
        // Buttons eat and drink
        imgButtons_drink[0] = findViewById(R.id.button_A2);
        imgButtons_drink[1] = findViewById(R.id.button_B2);

        for (ImageButton button : imgButtons_when) {
            button.setVisibility(View.INVISIBLE);
        }

        for (ImageButton button : imgButtons_drink) {
            button.setVisibility(View.INVISIBLE);
        }
    }

    public void onImageClick(View view) {
        if(!loopRunning || isLongPressing) return;

        ImageButton clickedButton = (ImageButton) view;
        int buttonId = clickedButton.getId();

        // Map button IDs to corresponding texts
        HashMap<Integer, String> buttonTextMap = new HashMap<>();
        buttonTextMap.put(R.id.button_A, "");
        buttonTextMap.put(R.id.button_B, "");
        buttonTextMap.put(R.id.button_A1, "QUE");
        buttonTextMap.put(R.id.button_B1, "CUANDO");
        buttonTextMap.put(R.id.button_A2, "BEBER");
        buttonTextMap.put(R.id.button_B2, "COMER");

        // Set visibility based on button ID
        if (buttonId == R.id.button_A) {
            for (ImageButton button : imgButtons) {
                button.setVisibility(View.INVISIBLE);
            }
            imgButtons_drink[0].setVisibility(View.VISIBLE);
            imgButtons_drink[1].setVisibility(View.VISIBLE);
            secondary_drink = true;
        }
        else if (buttonId == R.id.button_B){
            for (ImageButton button : imgButtons) {
                button.setVisibility(View.INVISIBLE);
            }
            imgButtons_when[0].setVisibility(View.VISIBLE);
            imgButtons_when[1].setVisibility(View.VISIBLE);
            secondary_when = true;
        }
        else {
            appendText(buttonTextMap.get(buttonId));
            secondary_drink = false;
            secondary_when = false;
            restartButtons();

        }
    }

    public void onButtonClick2(View view) {
        if(!loopRunning) return;

        Button clickedButton = (Button) view;
        String[] words = clickedButton.getText().toString().split(" ");

        if(words.length == 1){
            if("BORRAR".equals(words[0])){
                enteredText.setText("");
            }
            else if("LEER".equals(words[0])){
                String textToRead = enteredText.getText().toString().trim();

                // Check if TextToSpeech is initialized and the specified text is not empty
                if (textToSpeech != null && !textToRead.isEmpty()) {
                    // Set up a HashMap to specify the utterance ID
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "readAloud");

                    // Speak the entered text
                    textToSpeech.speak(textToRead, TextToSpeech.QUEUE_FLUSH, params);
                }}
            else if("INICIO".equals(words[0])){
                stopButtonLoop();
                Intent intent = new Intent(PictogramActivity.this, MainActivity.class);
                mainHandler.removeCallbacksAndMessages(null);
                startActivity(intent);
            }
            else if("REINICIAR".equals(words[0])){
                restartButtons();}
            restartButtonsConfigurations();
        }else{
            String firstButton = "", secondButton="";
            for(int i = 0 ; i < words.length  ; i++){
                if ( i < words.length / 2)
                    firstButton += words[i] ;
                else
                    secondButton += words[i];
            }
            this.configButtons[0].setText(firstButton);
            this.configButtons[1].setText(secondButton);
        }
    }

    protected void restartButtonsConfigurations(){
        int index = 0;
        for(String initialText: this.initialButtonTexts2){
            this.configButtons[index++].setText(initialText);}}


    private void initializeConfigCarrousel(){
        this.configButtons = new Button[2];

        Button deleteAllButton = findViewById(R.id.button_delete);
        deleteAllButton.setVisibility(View.INVISIBLE);

        Button ReadAloud = findViewById(R.id.button_read_out_loud);
        ReadAloud.setVisibility(View.INVISIBLE);

        this.configButtons[0] = ReadAloud;
        this.configButtons[1] = deleteAllButton;

    }

    private void restartButtons(){
        for (ImageButton button : imgButtons) {
            button.setVisibility(View.VISIBLE);
        }
        for (ImageButton button : imgButtons_when) {
            button.setVisibility(View.INVISIBLE);
        }
        for (ImageButton button : imgButtons_drink) {
            button.setVisibility(View.INVISIBLE);
        }
    }

    protected void appendText(String text) {
        enteredText.append(text);
        stopButtonLoop();
        setInitialButtonAsVisible();
    }

    private void stopButtonLoop() {
        loopRunning = false;
        for (ImageButton button : imgButtons) {
            button.setVisibility(View.INVISIBLE);}
    }

    private void setInitialButtonAsVisible() {
        loopRunning = true;
        if(!configCarrouselActivated){
            for (ImageButton button : imgButtons) {
                button.setVisibility(View.VISIBLE);
            }
        }
        else {setButtonVisibility(currentButtonIndex,View.VISIBLE);}
    }

    private void performLongClick() {
        loopRunning = false;

        if (!configCarrouselActivated) {
            for (ImageButton button : imgButtons) {
                button.setVisibility(View.INVISIBLE);
            }
            for (ImageButton button : imgButtons_drink) {
                button.setVisibility(View.INVISIBLE);
            }
            for (ImageButton button : imgButtons_when) {
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

    private void handleButtonLoop() {
        runOnUiThread(() -> {
            if(!configCarrouselActivated){
                if(!secondary_when && !secondary_drink) {
                    setButtonEnable(currentButtonIndex, false);
                    currentButtonIndex = (currentButtonIndex + 1) % imgButtons.length;
                    setButtonEnable(currentButtonIndex, true);
                } else if(secondary_when){
                    setButtonEnable(currentButtonIndex, false);
                    currentButtonIndex = (currentButtonIndex + 1) % imgButtons_when.length;
                    setButtonEnable(currentButtonIndex, true);
                } else if(secondary_drink){
                    setButtonEnable(currentButtonIndex, false);
                    currentButtonIndex = (currentButtonIndex + 1) % imgButtons_drink.length;
                    setButtonEnable(currentButtonIndex, true);
                }
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
        Toast.makeText(PictogramActivity.this, message, Toast.LENGTH_SHORT).show();
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
        }
    }

    private void handleSensorData() {
        String receivedData = SensorDataApplication.getSensorData();
        if ("0".equals(receivedData)) {
            pressVisibleButton();
        } else if ("2".equals(receivedData)) {
            performLongClick();
        }
        startSensorDataCheck(); // Reschedule the check
    }

    private void pressVisibleButton() {
        ImageButton visibleButton;
        Button visibleConfigButton;

        if(!configCarrouselActivated) {
            if (!secondary_when && !secondary_drink) {
                visibleButton = imgButtons[currentButtonIndex];
                runOnUiThread(() -> visibleButton.performClick());
            } else if (secondary_when) {
                visibleButton = imgButtons_when[currentButtonIndex];
                runOnUiThread(() -> visibleButton.performClick());
            } else if (secondary_drink) {
                visibleButton = imgButtons_drink[currentButtonIndex];
                runOnUiThread(() -> visibleButton.performClick());
            }
        } else {
            visibleConfigButton = configButtons[currentButtonIndex];
            runOnUiThread(() -> visibleConfigButton.performClick());
        }
    }

    private void setButtonVisibility(int index, int visibility) {
        if (!configCarrouselActivated) {
            if (!secondary_drink && !secondary_when) {
                if (index >= 0 && index < imgButtons.length) {
                    imgButtons[index].setVisibility(visibility);
                }
            } else if (secondary_when) {
                if (index >= 0 && index < imgButtons_when.length) {
                    imgButtons_when[index].setVisibility(visibility);
                }
            } else if (secondary_drink) {
                if (index >= 0 && index < imgButtons_drink.length) {
                    imgButtons_drink[index].setVisibility(visibility);
                }
            }
        } else {
            if (index >= 0 && index < configButtons.length) {
                configButtons[index].setVisibility(visibility);
            }
        }
    }

    private void setButtonEnable(int index, boolean isEnabled) {
        if (!configCarrouselActivated) {
            if (!secondary_drink && !secondary_when) {
                if (index >= 0 && index < imgButtons.length) {
                    imgButtons[index].setEnabled(isEnabled);
                }
            } else if (secondary_when) {
                if (index >= 0 && index < imgButtons_when.length) {
                    imgButtons_when[index].setEnabled(isEnabled);
                }
            } else if (secondary_drink) {
                if (index >= 0 && index < imgButtons_drink.length) {
                    imgButtons_drink[index].setEnabled(isEnabled);
                }
            }
        } else {
            if (index >= 0 && index < configButtons.length) {
                configButtons[index].setEnabled(isEnabled);
            }
        }
    }


    private void startSensorDataCheck(){
        mainHandler.sendEmptyMessageDelayed(Constants.CHECK_SENSOR_DATA, Constants.CHECK_INTERVAL);
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
