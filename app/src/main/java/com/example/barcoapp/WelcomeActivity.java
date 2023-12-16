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

        // Delayed transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity
                Intent mainIntent = new Intent(WelcomeActivity.this, BluetoothActivity.class);
                startActivity(mainIntent);

                // Finish the welcome activity
                finish();
            }
        }, 5000); // 5000 milliseconds (5 seconds) delay
    }
}

