package com.marktony.drinkingwater.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by lizhaotailang on 2015/11/18.
 * 判断NotiService是否在运行中
 */
public class JudgeServiceIsRunning {

    //检测service是否正在运行
    public static boolean isMyServiceRunning(Context context){
        ActivityManager manager = (ActivityManager)  context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.marktony.drinkingwater.NotiService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
