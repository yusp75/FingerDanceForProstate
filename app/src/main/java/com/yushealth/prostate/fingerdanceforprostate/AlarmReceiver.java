package com.yushealth.prostate.fingerdanceforprostate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "It's time to practice now.", Toast.LENGTH_LONG).show();
    }
}
