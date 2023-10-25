package com.example.barcoapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class LoopActivity extends AppCompatActivity {
    protected Integer[] carousel1ButtonsId; // Botones del primer carrusel
    protected Integer[] carousel2ButtonsId; // Botones del segundo carrusel
    protected Integer[] currentButtonsId;
    private boolean isNewCarousel = false; //Inicia el teclado de letras
    private int layoutId;
    private EditText enteredText;
    private Handler handler = new Handler();
    private Handler longPressHandler = new Handler();
    private boolean isLongPressing = false;

    // Button initialization
    private Integer[] buttonsId;
    private Button[] buttons;
    private int currentButtonIndex = 0;
    public boolean loopRunning = false;

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

        startButtonLoop();
        startLoop();
    }

    private void setButtonVisibility(int index, int visibility) {
        if (index >= 0 && index < buttons.length) {
            buttons[index].setVisibility(visibility);
        }
    }

    public void startButtonLoop() {
        loopRunning = true;
        setButtonVisibility(currentButtonIndex, View.VISIBLE);
    }

    public void startLoop() {
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

    public void stopButtonLoop() {
        loopRunning = false;
        setButtonVisibility(currentButtonIndex, View.INVISIBLE);
    }

    public void setButtons(Integer[] buttonIds) {
        buttonsId = buttonIds;
        buttons = new Button[buttonsId.length];
        for (int index = 0; index < buttonIds.length; index++) {
            buttons[index] = findViewById(buttonIds[index]);
        }
    }

    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            isNewCarousel = !isNewCarousel; // Cambiar entre carruseles al realizar un "long press"
            if (isNewCarousel) {
                currentButtonsId = carousel2ButtonsId; // Cambiar al segundo carrusel (opciones)
            } else {
                currentButtonsId = carousel1ButtonsId; // Cambiar al primer carrusel (letras/números)
            }
            setButtons(currentButtonsId);
        }
    };
}


