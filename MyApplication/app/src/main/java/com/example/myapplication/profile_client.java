package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.alterac.blurkit.BlurLayout;

public class profile_client extends AppCompatActivity {
    BlurLayout blurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_client);
        blurLayout = findViewById(R.id.blur_layout);

    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }
}