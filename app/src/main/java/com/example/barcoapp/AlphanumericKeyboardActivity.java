package com.example.barcoapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AlphanumericKeyboardActivity extends LoopActivity {
  public AlphanumericKeyboardActivity(){
      super(new Integer[]{
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
      }, R.layout.alphanumeric_layout);
  }
}