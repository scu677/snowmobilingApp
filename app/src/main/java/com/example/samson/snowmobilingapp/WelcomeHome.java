package com.example.samson.snowmobilingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomeHome extends Activity {
    private static int SPLASH_TIME_OUT=4000;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.welcome_home);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent welcomeIntent = new Intent(WelcomeHome.this, MainActivity.class);
                startActivity(welcomeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }

}
