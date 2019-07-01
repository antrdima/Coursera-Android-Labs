package com.example.dailyselfie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("path");

        Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(imageBitmap);
    }
}
