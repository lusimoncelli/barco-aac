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
        }, R.layout.numbers_layout, new String[]{"01234", "56789"});
    }

}