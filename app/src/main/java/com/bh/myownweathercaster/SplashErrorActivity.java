package com.bh.myownweathercaster;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashErrorActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_error);

        getWindow().setStatusBarColor(Color.rgb(255,255,255));
    }

}