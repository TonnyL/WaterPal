package com.marktony.drinkingwater.activities;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2015/9/24.
 * 此类的作用是实现对所有活动的管理，可以方便的添加，移除，以及全部销毁
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities){
            if (activity.isFinishing()){
                activity.finish();
            }
        }
    }

}
