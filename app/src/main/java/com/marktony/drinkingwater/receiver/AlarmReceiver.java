package com.marktony.drinkingwater.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.marktony.drinkingwater.service.NotiService;

/**
 * Created by lizhaotailang on 2015/10/6.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,NotiService.class);
        context.startService(i);
    }
}
