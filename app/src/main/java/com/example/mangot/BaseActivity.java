package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {

    private WiFiReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        receiver = new WiFiReceiver();

    }


    @Override
    protected void onResume() {
        super.onResume();
        // register to wifi broadcasts - specify WIFI
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver,filter);


    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister
        unregisterReceiver(receiver);
    }
}