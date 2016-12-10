package com.gov.phcrevitalization.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gov.phcrevitalization.R;
import com.gov.phcrevitalization.utils.Opener;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Opener.LandingActivity(SplashScreen.this);
        finish();

    }
}
