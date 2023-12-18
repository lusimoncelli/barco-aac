package com.example.barcoapp;

import android.view.View;
import android.widget.Button;

public class PictogramActivity extends LoopActivity {
    public PictogramActivity() {
        super(new Integer[]{
                        R.id.button_A,
                        R.id.button_B
                }, R.layout.pictogram_layout, new String[]{"CASA PERRO", "ARBOL PAPEL"}
        );
    }

    @Override
    public void onButtonClick(View view) {
        if(!loopRunning || isLongPressing) return;

        Button clickedButton = (Button) view;
        String[] words = clickedButton.getText().toString().split(" ");
        if( words.length == 1){
            appendText(words[0]+" ");
            restartButtons();
        }else{
            String firstButton = "", secondButton="";
            for(int i = 0 ; i < words.length  ; i++){
                if ( i < words.length / 2)
                    firstButton += words[i];
                else
                    secondButton += words[i];

            }
            this.buttons[0].setText(firstButton);
            this.buttons[1].setText(secondButton);
        }
    }
}
