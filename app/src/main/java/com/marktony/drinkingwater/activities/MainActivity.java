package com.marktony.drinkingwater.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import com.marktony.drinkingwater.fragment.NavigationDrawerFragment;
import com.marktony.drinkingwater.service.NotiService;
import com.marktony.drinkingwater.R;
import com.marktony.drinkingwater.util.JudgeServiceIsRunning;
import com.marktony.drinkingwater.util.ShortcutUtil;
import com.marktony.drinkingwater.fragment.MainFragment_section1;
import com.marktony.drinkingwater.fragment.MainFragment_section2;
import com.marktony.drinkingwater.fragment.MainFragment_section3;
import com.marktony.drinkingwater.fragment.MainFragment_section4;
import com.marktony.drinkingwater.fragment.MainFragment_section5;
import com.marktony.drinkingwater.fragment.MainFragment_section6;
import com.umeng.update.UmengUpdateAgent;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private NavigationDrawerFragment mNavigationDrawerFragment;//Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private CharSequence mTitle;//用于储存最后一次屏幕的标题


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();

        if(sharedPreferences.getBoolean("section4_sw_notifications",true) && JudgeServiceIsRunning.isMyServiceRunning(this)){
            //启动Service
            Intent intent = new Intent(MainActivity.this,NotiService.class);
            startService(intent);
        }

        //umeng检测是否有新版本
        if (sharedPreferences.getBoolean("Auto_update",true)){
            UmengUpdateAgent.update(this);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

         //Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //强制显示overflow菜单在左上角
       forceShowOverflowMenu();

        if (!sharedPreferences.getBoolean("Launched",false)){
            //创建桌面快捷方式
            ShortcutUtil.creatShortCut(this, R.mipmap.ic_launcher, R.string.app_name);
            editor.putBoolean("Launched",true);
            editor.commit();
        }

    }


    //通过position的不同对container中的内容进行操作
    //此处注意onNavigationDrawerItemSelected函数与onSectionAttached函数的参数的一一对应关系
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment fragment = new MainFragment_section1();
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position){
            case 0:
                fragment = new MainFragment_section1();
                onSectionAttached(1);
                restoreActionBar();
                break;
            case 1:
                fragment = new MainFragment_section2();
                onSectionAttached(2);
                restoreActionBar();
                break;
            case 2:
                fragment = new MainFragment_section3();
                onSectionAttached(3);
                restoreActionBar();
                break;
            case 3:
                fragment = new MainFragment_section4();
                onSectionAttached(4);
                restoreActionBar();
                break;
            case 4:
                fragment = new MainFragment_section5();
                onSectionAttached(5);
                restoreActionBar();
                break;
            case 5:
                fragment = new MainFragment_section6();
                onSectionAttached(6);
                restoreActionBar();
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {

        if (number == 1){
            mTitle = getString(R.string.title_section1_title);
        }else if (number == 2){
            mTitle = getString(R.string.title_section2);
        }else if (number == 3){
            mTitle = getString(R.string.title_section3);
        }else if (number == 4){
            mTitle = getString(R.string.title_section4);
        }else if (number == 5){
            mTitle = getString(R.string.title_setcion5);
        }else if (number == 6){
            mTitle = getString(R.string.title_section6);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * 如果设备有物理菜单按键，需要将其屏蔽才能显示OverflowMenu
     */
    private void forceShowOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitBy2Click();//调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {

        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this,getString(R.string.exitBy2Click_toast) , Toast.LENGTH_SHORT).show();
            mNavigationDrawerFragment.mDrawerLayout.openDrawer(Gravity.LEFT);//用户点击back键之后自动打开抽屉
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ActivityCollector.finishAll();
            System.exit(0);
        }
    }

}
