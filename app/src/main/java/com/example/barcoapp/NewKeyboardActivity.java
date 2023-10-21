package com.example.barcoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class NewKeyboardActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 123;
    private Button addPhotoButton;
    private Button buttonSettings;
    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_keyboard_layout);

        addPhotoButton = findViewById(R.id.addPhotoButton);
        buttonSettings = findViewById(R.id.button_settings);
        imageView = findViewById(R.id.image_add_photo);

        // Go to Settings
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewKeyboardActivity.this, BluetoothActivity.class);
                startActivity(intent);
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            // Do something with the image here, such as display it in an ImageView
        }
    }
}
