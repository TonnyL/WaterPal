package com.marktony.drinkingwater.fragment;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marktony.drinkingwater.MyDatabaseHelper;
import com.marktony.drinkingwater.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * 关联内容为首页
 */
public class MainFragment_section1 extends Fragment{

    private View view;
    private FloatingActionButton button;
    private ProgressBar progressBar;
    private TextView textView2,textView3;//textview3的数据根据系统所处的时间不同会做相应的变化,仍然需要修改
    private int target;//应该喝水的总量,此处在后期建立数据库之后从数据库中获取相关的数据
    private int progress;//临时保存喝水量
    private int time_Day;//用于判断时间，如果时间和当前时间不同，则将当前时间赋值给time
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MainFragment_section1() {
        //要求一个空的构造函数
    }

    /**
     * 用户第一次点击+号按钮时提醒用户添加体重及水杯大小数据
     *通过点击加号按钮进行数据的添加
     * 达到指定喝水量后提示用户是否进行分享
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        //由于在mainactivity中关闭了optionmenu,需要在fragment中开启
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_main_fragment_section1, container, false);

        button = (FloatingActionButton) view.findViewById(R.id.section1_btnMain);//关联+号按钮
        progressBar = (ProgressBar) view.findViewById(R.id.section1_pgsBar);//关联喝水进度条
        textView2 = (TextView) view.findViewById(R.id.section1_tv2);//关联喝水进度
        textView3 = (TextView) view.findViewById(R.id.section1_tv3);//关联提示语句

        sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE).edit();

        //如果当前日期的天数和time_Day相等，则从sharedpreferences中读取progress的值
        //如果当前日期的天数和time_Day不想等，则将progress的值赋值为0并且储存到sharedpreferences中
        time_Day = sharedPreferences.getInt("Time_Day",0);
        if (time_Day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
            progress = sharedPreferences.getInt("Progress", 0);
        }else {
            progress = 0;
            time_Day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            editor.putInt("Time_Day",time_Day);
            editor.putInt("Progress", progress);
            editor.commit();
        }

        target = sharedPreferences.getInt("AdvicedWaterAmount",0);
        progressBar.setMax(target);
        textView2.setText("" + progress + "/" + target + "ml");
        progressBar.setProgress(progress);

        final MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(getActivity(),"History.db",null,1);
        //建立喝水记录数据库
        myDatabaseHelper.getWritableDatabase();

        /**
         * 如果应用是第一次打开，那么将assets目录下的数据库文件移动到文件存储的地方
         * /data/data/com.marktony.drinkingwater/files/数据库文件
         * 同时新建一个数据库，History，用于保存用户的喝水记录
         * 否则程序继续执行
         */
        final String DB_PATH = "/data/data/com.marktony.drinkingwater/databases";//数据库保存路径
        final String DB_NAME = "advicedwateramount.db";//建议喝水量数据库名称

        //复制喝水量数据库
        if (new File(DB_PATH + DB_NAME).exists() == false){
            File dir = new File(DB_PATH);
            if (dir.exists()){
                dir.mkdir();
            }

            try {
                InputStream is = getActivity().getBaseContext().getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
                byte[] buffer = new byte[1024];
                int length;

                while ((length = is.read(buffer)) > 0){
                    os.write(buffer,0,length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //判断应用是否为第一次启动，以及启动的时间来设置相应的提示语句
        if (sharedPreferences.getBoolean("IsFirstLaunch",true)) {
            textView3.setText(R.string.section1_tv3_firstlaunch);
        }
        if (!sharedPreferences.getBoolean("IsFirstLaunch",true)){
            Calendar calendar = Calendar.getInstance();
            ContentResolver cv = getActivity().getContentResolver();
            String strTimeFormat = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);
            if(strTimeFormat != null && strTimeFormat.equals("24")) {

                //0点到9点
                if (calendar.get(Calendar.HOUR_OF_DAY) >= 0 && (calendar.get(Calendar.HOUR_OF_DAY) <= 9)){
                    textView3.setText(R.string.section1_tv3_addone);
                }
                if (calendar.get(Calendar.HOUR_OF_DAY) >= 10 && (calendar.get(Calendar.HOUR_OF_DAY) <= 12)) {
                    textView3.setText(R.string.section1_tv3_morning);
                }
                if (calendar.get(Calendar.HOUR_OF_DAY) >= 13 && (calendar.get(Calendar.HOUR_OF_DAY) <= 14)) {
                    textView3.setText(R.string.section1_tv3_noon);
                }
                if (calendar.get(Calendar.HOUR_OF_DAY) >= 15 && (calendar.get(Calendar.HOUR_OF_DAY) <= 18)) {
                    textView3.setText(R.string.section1_tv3_afternoon);
                }
                if (calendar.get(Calendar.HOUR_OF_DAY) >= 19 && (calendar.get(Calendar.HOUR_OF_DAY) <= 21)) {
                    textView3.setText(R.string.section1_tv3_evening);
                }
                if (calendar.get(Calendar.HOUR_OF_DAY) >= 22 && (calendar.get(Calendar.HOUR_OF_DAY) <= 23)){
                    textView3.setText(R.string.section1_tv3_addone);
                }
            }

            if(strTimeFormat != null && strTimeFormat.equals("12")){
                if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                    if ((calendar.get(Calendar.HOUR_OF_DAY) >= 0) && (calendar.get(Calendar.HOUR_OF_DAY) <= 9)){
                        textView3.setText(R.string.section1_tv3_addone);
                    }
                    if ((calendar.get(Calendar.HOUR_OF_DAY) >= 10) && (calendar.get(Calendar.HOUR_OF_DAY) <= 11)) {
                        textView3.setText(R.string.section1_tv3_morning);
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                        textView3.setText(R.string.section1_tv3_morning);
                    }
                }

                if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                    if ((calendar.get(Calendar.HOUR_OF_DAY) >= 1) && (calendar.get(Calendar.HOUR_OF_DAY) <= 2)) {
                        textView3.setText(R.string.section1_tv3_noon);
                    }
                    if ((calendar.get(Calendar.HOUR_OF_DAY) >= 3) && (calendar.get(Calendar.HOUR_OF_DAY) <= 6)) {
                        textView3.setText(R.string.section1_tv3_afternoon);
                    }
                    if ((calendar.get(Calendar.HOUR_OF_DAY) >= 7) && (calendar.get(Calendar.HOUR_OF_DAY) <= 9)) {
                        textView3.setText(R.string.section1_tv3_evening);
                    }
                    if ((calendar.get(Calendar.HOUR_OF_DAY) >= 10) && (calendar.get(Calendar.HOUR_OF_DAY) <= 11)){
                        textView3.setText(R.string.section1_tv3_addone);
                    }
                }
            }
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean("IsFirstLaunch", true) == false){
                    progress = progress + sharedPreferences.getInt("Bottle", 0);
                    progressBar.setProgress(progress);

                    editor.putInt("Progress", progress);
                    editor.commit();
                    textView2.setText("" + progress + "/" + target + "ml");

                    ContentResolver cv =  getActivity().getBaseContext().getContentResolver();
                    String strTimeFormat = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);

                    SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();

                    if(strTimeFormat != null && strTimeFormat.equals("24")){
                        values.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));//获取当前系统的年份、月份和日期并储存
                        values.put("bottle", String.valueOf(sharedPreferences.getInt("Bottle", 0)));
                        values.put("time", new SimpleDateFormat("HH:mm").format(new java.util.Date()));//获取当前系统的时间-小时和分钟并储存
                        values.put("week", new SimpleDateFormat("EEEE").format(new java.util.Date()));//获取当前为星期几并储存

                        db.insert("History", null, values);
                    }

                    //12小时制时
                    if(strTimeFormat != null && strTimeFormat.equals("12")){
                        String am = getString(R.string.am);
                        String pm = getString(R.string.pm);
                        Calendar calendar = Calendar.getInstance();
                        values.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));//获取当前系统的年份、月份和日期并储存
                        values.put("bottle", String.valueOf(sharedPreferences.getInt("Bottle", 0)));
                        if (calendar.get(Calendar.AM_PM) == 0){
                            values.put("time", new SimpleDateFormat("hh:mm").format(new java.util.Date()) + am);//获取当前系统的时间-小时和分钟并储存
                        }else{
                            values.put("time", new SimpleDateFormat("hh:mm").format(new java.util.Date()) + pm);//获取当前系统的时间-小时和分钟并储存
                        }
                        values.put("week", new SimpleDateFormat("EEEE").format(new java.util.Date()));//获取当前为星期几并储存
                        db.insert("History", null, values);
                    }
                    Toast.makeText(getActivity(), R.string.section1_toast, Toast.LENGTH_SHORT).show();

                    if ((progress >= target) && (progress != 0)) {
                        textView2.setText("" + progress + "/" + target + "ml");
                        if (sharedPreferences.getBoolean("Congratulate", true)) {

                            final com.rey.material.app.Dialog dialog = new com.rey.material.app.Dialog(getActivity());
                            dialog.setTitle(R.string.section1_dialog_t);
                            dialog.setContentView(R.layout.congratulation_dialog);
                            dialog.positiveAction(R.string.section1_dialog_p);
                            dialog.negativeAction(R.string.section1_dialog_n);
                            dialog.positiveActionClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.negativeActionClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.share_intent_text1));
                                    sendIntent.setType("text/plain");
                                    startActivity(Intent.createChooser(sendIntent, getText(R.string.share_intent_title)));
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }
                    }

                    if (progress > 13200){
                        Toast.makeText(getActivity(),getString(R.string.toast_alert),Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.section_share,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.section_share){
            //仍然需要优化，设置不同的分享内容
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.share_intent_text1));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getText(R.string.share_intent_title)));
        }

        return super.onOptionsItemSelected(item);

    }
}
