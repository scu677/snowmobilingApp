package com.example.samson.snowmobilingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class OfflineMaps extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
