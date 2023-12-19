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
    private boolean configCarrouselActivated = false;
    private EditText enteredText;

    // Button initialization
    private Button[] configButtons;
    private final ImageButton[] imgButtons = new ImageButton[2];
    private final ImageButton[] secondary_imgButtons = new ImageButton[4];
    private int currentButtonIndex = 0;
    protected boolean loopRunning = false;
    protected boolean secondary = false;
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

        secondary_imgButtons[0] = findViewById(R.id.button_A1);
        secondary_imgButtons[1] = findViewById(R.id.button_B1);
        secondary_imgButtons[2] = findViewById(R.id.button_A2);
        secondary_imgButtons[3] = findViewById(R.id.button_B2);

        for (ImageButton button : secondary_imgButtons) {
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void onButtonClick(View view) {
        ImageButton clickedButton = (ImageButton) view;
        if(clickedButton.getId() == R.id.button_A) {
            secondary_imgButtons[0].setVisibility(View.VISIBLE);
            secondary_imgButtons[1].setVisibility(View.VISIBLE);
            for (ImageButton button : imgButtons) {
                button.setVisibility(View.INVISIBLE);
            }
            secondary = true;
        } else if(clickedButton.getId() == R.id.button_B) {
            secondary_imgButtons[2].setVisibility(View.VISIBLE);
            secondary_imgButtons[3].setVisibility(View.VISIBLE);
            for (ImageButton button : imgButtons) {
                button.setVisibility(View.INVISIBLE);
            }
            secondary = true;
        } else if(clickedButton.getId() == R.id.button_A1) {
            appendText("QUE");
            restartButtons();
            secondary = false;
        } else if(clickedButton.getId() == R.id.button_B1) {
            appendText("CUANDO");
            restartButtons();
            secondary = false;
        }else if(clickedButton.getId() == R.id.button_A2) {
            appendText("COMER");
            restartButtons();
            secondary = false;
        } else if(clickedButton.getId() == R.id.button_B2) {
            appendText("BEBER");
            restartButtons();
            secondary = false;
        }
    }

    private void initializeConfigCarrousel(){
        this.configButtons = new Button[4];

        Button backButton = findViewById(R.id.button_back_to_main);
        backButton.setVisibility(View.INVISIBLE);
        backButton.setOnClickListener(v -> {
            if(! isLongPressing){
                Intent intent = new Intent(PictogramActivity.this, MainActivity.class);
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

    private void restartButtons(){
        for (ImageButton button : imgButtons) {
            button.setVisibility(View.VISIBLE);
        }
        for (ImageButton button : secondary_imgButtons) {
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
            for (ImageButton button : secondary_imgButtons) {
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
            if (!configCarrouselActivated && !secondary) {
                setButtonEnable(currentButtonIndex, false);
                currentButtonIndex = (currentButtonIndex + 1) % imgButtons.length;
                setButtonEnable(currentButtonIndex, true);
            } else if (!configCarrouselActivated && secondary) {
                // Check if it's A1 or B1 button and adjust visibility accordingly
                if (currentButtonIndex == 0 || currentButtonIndex == 1) {
                    setButtonEnable(currentButtonIndex, false);
                    currentButtonIndex = (currentButtonIndex + 2) % secondary_imgButtons.length;
                    setButtonEnable(currentButtonIndex, true);
                } else {
                    setButtonEnable(currentButtonIndex, false);
                    currentButtonIndex = (currentButtonIndex + 1) % secondary_imgButtons.length;
                    setButtonEnable(currentButtonIndex, true);
                }
            } else {
                // Check if it's A2 or B2 button and adjust visibility accordingly
                if (currentButtonIndex == 2 || currentButtonIndex == 3) {
                    setButtonEnable(currentButtonIndex, false);
                    currentButtonIndex = (currentButtonIndex + 2) % secondary_imgButtons.length;
                    setButtonEnable(currentButtonIndex, true);
                } else {
                    setButtonVisibility(currentButtonIndex, View.INVISIBLE);
                    currentButtonIndex = (currentButtonIndex + 1) % configButtons.length;
                    setButtonVisibility(currentButtonIndex, View.VISIBLE);
                }
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

        if(!configCarrouselActivated && !secondary) {
            visibleButton = imgButtons[currentButtonIndex];
            runOnUiThread(() -> visibleButton.performClick());
        } else if(!configCarrouselActivated && secondary){
            visibleButton = imgButtons[currentButtonIndex];
            runOnUiThread(() -> visibleButton.performClick());
        } else {
            visibleConfigButton = configButtons[currentButtonIndex];
            runOnUiThread(() -> visibleConfigButton.performClick());
        }
    }

    private void setButtonVisibility(int index, int visibility) {
        if(!configCarrouselActivated && !secondary){
            if (index >= 0 && index < imgButtons.length){
                imgButtons[index].setVisibility(visibility);
            }

        } else {
            if (index >= 0 && index < configButtons.length)
                configButtons[index].setVisibility(visibility);
        }
    }

    private void setButtonEnable(int index, boolean isEnabled) {
        if (!configCarrouselActivated) {
            if (index >= 0 && index < imgButtons.length) {
                imgButtons[index].setEnabled(isEnabled);
            }
        } else {
            if (index >= 0 && index < configButtons.length) {
                configButtons[index].setEnabled(isEnabled);
            }
        }
    }

    public ImageButton getButton() {
        return imgButtons[currentButtonIndex];
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
