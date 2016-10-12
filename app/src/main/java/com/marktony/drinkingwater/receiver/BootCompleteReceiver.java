package com.marktony.drinkingwater.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.marktony.drinkingwater.activities.MainActivity;

/**
 * Created by lizhaotailang on 2015/10/24.
 * 广播接收器：接收开机完成的广播
 * 实现开机自动启动Service
 */
public class BootCompleteReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);

    }
}
