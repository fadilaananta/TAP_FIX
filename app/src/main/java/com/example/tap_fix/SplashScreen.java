package com.example.tap_fix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SplashScreen extends AppCompatActivity {

    ImageButton splashscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashscreen = findViewById(R.id.imgbutton_splash);

        splashscreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(SplashScreen.this, LoginActivity.class);
                SplashScreen.this.startActivity(activityChangeIntent);
            }
        });
    }
}