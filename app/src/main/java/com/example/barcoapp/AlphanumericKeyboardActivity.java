package com.example.barcoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AlphanumericKeyboardActivity extends LoopActivity {
    public AlphanumericKeyboardActivity() {
        super(new Integer[]{
                        R.id.button_A,
                        R.id.button_B
                }, R.layout.alphanumeric_layout, new String[] {"ABCDEFGHIJKLM", "NOPQRSTUVWXYZ"}, new String[] {"BORRAR LEER", "INICIO REINICIAR"}
        );
    }

}