package com.example.barcoapp;

public class NumbersKeyboardActivity extends LoopActivity {
    public NumbersKeyboardActivity(){
        super(new Integer[]{
                R.id.button_01234,
                R.id.button_56789,
        }, R.layout.numbers_layout, new String[]{"01234", "56789"});
    }

}