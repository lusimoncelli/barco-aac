package com.example.barcoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class NewKeyboardActivity extends AppCompatActivity {

    private Button addPhotoButton;
    private Button buttonSettings;
    private Button buttonMenu;
    private TableLayout layoutButtons;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_keyboard_layout);

        addPhotoButton = findViewById(R.id.addPhotoButton);
        buttonSettings = findViewById(R.id.button_settings);
        buttonMenu = findViewById(R.id.button_inicio);

        layoutButtons = findViewById(R.id.layout_buttons);

        // Go to Settings
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewKeyboardActivity.this, BluetoothActivity.class);
                startActivity(intent);
            }
        });

        // Go to main screen
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewKeyboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri imageUri = data.getData();
                    if (imageUri != null) {
                        createButtonWithImage(imageUri);
                    }
                }
            }
        });

        // Open Gallery to add photo
        addPhotoButton.setOnClickListener(view -> openGallery());

    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }


    private void createButtonWithImage(Uri imageUri) {
        // Create a new button
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new TableRow.LayoutParams(150, 150));
        imageView.setImageURI(imageUri);

        // Create a new row in the table layout and add the ImageView
        TableRow newRow = new TableRow(this);
        newRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        newRow.addView(imageView);

        // Add the new row to the table layout
        layoutButtons.addView(newRow);
    }
}






