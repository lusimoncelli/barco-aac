package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
         new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(WelcomeActivity.this, BluetoothActivity.class);
            startActivity(mainIntent);
        }, 5000);
    }
}

