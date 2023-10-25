package com.example.barcoapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class LoopActivity extends AppCompatActivity {
    public Integer[] carousel1ButtonsId; // Botones del primer carrusel
    public Integer[] carousel2ButtonsId; // Botones del segundo carrusel
    public Integer[] currentButtonsId;
    private boolean isNewCarousel = false; //Inicia el teclado de letras
    private int layoutId;
    private EditText enteredText;
    private Handler handler = new Handler();
    private Handler longPressHandler = new Handler();
    private boolean isLongPressing = false;

    // Button initialization
    private Button[] buttons1;
    private Button[] buttons2;
    private int currentButtonIndex = 0;
    public boolean loopRunning = false;

    protected LoopActivity(Integer[] currentButtonsId, int layoutId) {
        this.currentButtonsId = currentButtonsId;
        this.buttons1 = new Button[carousel1ButtonsId.length];
        this.buttons2 = new Button[carousel2ButtonsId.length];

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
        for (Integer currentButtonsId : this.carousel1ButtonsId)
            this.buttons1[index++] = findViewById(currentButtonsId);
        for (Button button : buttons1) {
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



        for (Integer currentButtonsId : this.carousel2ButtonsId)
            this.buttons2[index++] = findViewById(currentButtonsId);
        for (Button button : buttons2) {
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
        if (index >= 0 && index < buttons1.length) {
            buttons1[index].setVisibility(visibility);
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
                currentButtonIndex = (currentButtonIndex + 1) % buttons1.length;
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

    public Button getButton() {
        return buttons1[currentButtonIndex];
    }

    public void setButtons(Integer[] currentButtonsId) {
        currentButtonsId = currentButtonsId;
        buttons1 = new Button[currentButtonsId.length];
        for (int index = 0; index < currentButtonsId.length; index++) {
            buttons1[index] = findViewById(currentButtonsId[index]);
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


