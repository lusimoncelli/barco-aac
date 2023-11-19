package com.example.barcoapp;

public class NewKeyboardActivity extends LoopActivity {
    public NewKeyboardActivity() {
        super(new Integer[]{
                        R.id.button_hola,
                        R.id.button_chau,
                        R.id.button_si,
                        R.id.button_no,
                }, R.layout.new_keyboard_layout, new String[] {"ABCDEFGHIJKLM", "NOPQRSTUVWXYZ"}
        );
    }

}



