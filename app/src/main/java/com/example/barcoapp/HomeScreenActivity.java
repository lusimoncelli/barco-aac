package com.example.barcoapp;

import android.view.View;
import android.widget.RelativeLayout;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class HomeScreenActivity extends AppCompatActivity {

    private RelativeLayout container;
    private int currentButtonIndex = 0;
    private Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_layout);

        container = findViewById(R.id.container);

        buttons = new Button[5];


        for (int i = 0; i < buttons.length; i++){
            buttons[i] = new Button(this);
            // TODO: change text on buttons, use strings.xml
            buttons[i].setText('Prueba');
            buttons[i].setVisibility(View.INVISIBLE);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: handle button when clicked
                }
            });
            container.addView(buttons[i]);
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
