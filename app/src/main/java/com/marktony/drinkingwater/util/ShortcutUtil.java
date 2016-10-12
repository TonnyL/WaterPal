package com.marktony.drinkingwater.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

/**
 * Created by lizhaotailang on 2015/10/27.
 * 在应用第一次启动后创建桌面快捷方式
 */
public class ShortcutUtil {

    public static void creatShortCut(Activity act, int iconResId,int appnameResId){
        // com.android.launcher.permission.INSTALL_SHORTCUT

        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);

        // 需要显示的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, act.getString(appnameResId));

        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(act.getApplicationContext(), iconResId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        // 点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(act.getApplicationContext(), act.getClass()));

        // 发送广播
        act.sendBroadcast(shortcutintent);
    }

}
