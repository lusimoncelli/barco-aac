package com.example.barcoapp;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private int currentButtonIndex = 0;
    private Button[] buttons;
    private String[] buttonTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Initialization
        final Button buttonSettings = findViewById(R.id.button_settings);


        // Go to Settings
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                startActivity(intent);
            }
        });

        buttonTexts = getResources().getStringArray(R.array.numbers_buttons);
        buttons = new Button[buttonTexts.length];

        for (int i = 0; i < buttons.length; i++){
            buttons[i] = new Button(this);
            // TODO: change text on buttons, use strings.xml
            buttons[i].setText(buttonTexts[i]);
            buttons[i].setVisibility(View.INVISIBLE);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: handle button when clicked
                }
            });
            }
        // Change button every 1 second
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentButtonIndex < buttons.length) {
                    buttons[currentButtonIndex].setVisibility(View.INVISIBLE);
                    currentButtonIndex++;
                    if (currentButtonIndex < buttons.length) {
                        buttons[currentButtonIndex].setVisibility(View.VISIBLE);
                        handler.postDelayed(this, 1000); // Repeat every 1 second
                    }
                }
            }
        };

        // Start initial change
        buttons[currentButtonIndex].setVisibility(View.VISIBLE);
        handler.postDelayed(runnable, 10000);
    }
}
