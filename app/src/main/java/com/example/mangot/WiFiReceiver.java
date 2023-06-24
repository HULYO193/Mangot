package com.example.mangot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;

public class WiFiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        Bundle extras = intent.getExtras();

        NetworkInfo info = (NetworkInfo) extras
                .getParcelable("networkInfo");

        NetworkInfo.State state = info.getState();

        if (state == NetworkInfo.State.CONNECTED) {
            Toast.makeText(context, "Internet connection is on", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Internet connection is Off", Toast.LENGTH_LONG).show();

        }


    }
}