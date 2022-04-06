package com.example.potholeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplashPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProgressBar pb;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        pb = findViewById(R.id.progress);
        final int[] total = {0};
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashPage.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }
}