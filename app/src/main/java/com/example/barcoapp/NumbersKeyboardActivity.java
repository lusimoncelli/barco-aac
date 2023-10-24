package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NumbersKeyboardActivity extends LoopActivity {
    public NumbersKeyboardActivity(){
        super(new Integer[]{
                R.id.button_01234,
                R.id.button_56789,

        }, R.layout.numbers_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button backButton = findViewById(R.id.button_back_to_main);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the behavior to return to the main activity here
                Intent intent = new Intent(NumbersKeyboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}