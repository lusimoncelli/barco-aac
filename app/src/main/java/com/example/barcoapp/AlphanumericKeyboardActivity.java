package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AlphanumericKeyboardActivity extends LoopActivity {
    private EditText enteredText;
    private int layoutId;

    public AlphanumericKeyboardActivity() {
        super(new Integer[0], R.layout.alphanumeric_layout);
        carousel1ButtonsId = new Integer[]{
                R.id.button_A,
                R.id.button_B,
                R.id.button_C,
                R.id.button_D,
                R.id.button_E,
                R.id.button_F,
                R.id.button_G,
                R.id.button_H,
                R.id.button_I,
                R.id.button_J,
                R.id.button_K,
                R.id.button_L,
                R.id.button_M,
                R.id.button_N,
                R.id.button_O,
                R.id.button_P,
                R.id.button_Q,
                R.id.button_R,
                R.id.button_S,
                R.id.button_T,
                R.id.button_U,
                R.id.button_V,
                R.id.button_W,
                R.id.button_X,
                R.id.button_Y,
                R.id.button_Z,
                R.id.button__,
        };

        carousel2ButtonsId = new Integer[]{
                R.id.button_DeleteChar,
                R.id.button_DeleteWord,
                R.id.button_BackToPrevKey,
                R.id.button_BackToMain,
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutId);
        enteredText = findViewById(R.id.enteredText);
        enteredText.setTextColor(getResources().getColor(R.color.black));
        enteredText.setVisibility(View.VISIBLE);

        Button toMainButton = findViewById(R.id.button_BackToMain);
        Button deleteCharButton = findViewById(R.id.button_DeleteChar);
        Button deleteWordButton = findViewById(R.id.button_DeleteWord);
        toMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define el comportamiento para volver a la actividad principal aquí
                Intent intent = new Intent(AlphanumericKeyboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Configurar los botones del carrusel1 para agregar letras al cuadro de texto
        for (Integer buttonId : carousel1ButtonsId) {
            Button button = findViewById(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (loopRunning) {
                        Button clickedButton = (Button) view;
                        String buttonText = clickedButton.getText().toString();
                        appendText(buttonText);
                    }
                }
            });
        }
        // Configura un OnClickListener para el botón DeleteChar
        deleteCharButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el texto actual del cuadro de texto
                String currentText = enteredText.getText().toString();
                // Verifica si el texto no está vacío
                if (!currentText.isEmpty()) {
                    // Elimina la última letra del texto
                    String newText = currentText.substring(0, currentText.length() - 1);
                    enteredText.setText(newText);
                }
            }
        });
        deleteWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredText.setText("");
            }
        });

        // Iniciar el bucle de botones y el bucle principal
        startButtonLoop();
        startLoop();
    }

    private void appendText(String text) {
        enteredText.append(text);
        stopButtonLoop();
        startButtonLoop();
    }
}
