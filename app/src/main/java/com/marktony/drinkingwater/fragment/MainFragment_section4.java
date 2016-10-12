package com.marktony.drinkingwater.fragment;



import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marktony.drinkingwater.R;
import com.marktony.drinkingwater.activities.MainActivity;
import com.marktony.drinkingwater.service.NotiService;
import com.marktony.drinkingwater.util.JudgeServiceIsRunning;


/**
 *关联内容为通知设置
 */
public class MainFragment_section4 extends Fragment{

    private View view;
    private com.rey.material.widget.Switch section4_sw_notifications;
    private com.rey.material.widget.Switch section4_sw_auto_noti;
    private TextView section4_tv_auto_noti;
    private TextView section4_tv_manual_noti;
    private LinearLayout section4_LL_Manual_noti;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MainFragment_section4() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main_fragment_section4, container, false);

        sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE).edit();

        section4_sw_notifications = (com.rey.material.widget.Switch) view.findViewById(R.id.section4_sw_notifications);//通知开关
        section4_sw_auto_noti = (com.rey.material.widget.Switch) view.findViewById(R.id.section4_sw_auto_noti);//自动提醒开关
        section4_tv_auto_noti = (TextView) view.findViewById(R.id.section4_tv_auto_noti);//自动提醒 文本
        section4_tv_manual_noti = (TextView) view.findViewById(R.id.section4_tv_manual_noti);//手动设置提醒时间 文本,后续添加
        section4_LL_Manual_noti = (LinearLayout) view.findViewById(R.id.section4_LL_Manual_noti);//手动设置提醒时间 文本，后续添加


        //初始化界面,根据sharedpreference中的值进行初始化
        //不论通知开关是否打开，手动设置提醒时间的区域均为不可点击
        if (sharedPreferences.getBoolean("section4_sw_notifications",true)){
            section4_sw_notifications.setChecked(true);
            if (sharedPreferences.getBoolean("section4_sw_auto_noti",true)){
                //自动开关为可点击状态，文字为黑色可点击状态
                section4_sw_auto_noti.setChecked(true);
                section4_tv_auto_noti.setTextColor(0xff585858);

                //手动开关为不可点击，文字为浅颜色的不可点击状态
                section4_LL_Manual_noti.setClickable(false);
                section4_tv_manual_noti.setTextColor(0xffcdcdcd);
            }else {
                //自动开关为不可点击状态，文字为浅色不可点击状态
                section4_sw_auto_noti.setChecked(false);
                section4_tv_auto_noti.setTextColor(0xffcdcdcd);

                //手动设置为可点击状态，颜色为深色可点击状态
                section4_LL_Manual_noti.setClickable(false);
                section4_tv_manual_noti.setTextColor(0xff585858);
            }
        }else {
            section4_sw_notifications.setChecked(false);
            section4_sw_auto_noti.setChecked(false);
            section4_tv_auto_noti.setTextColor(0xffcdcdcd);
            section4_LL_Manual_noti.setClickable(false);
            section4_tv_manual_noti.setTextColor(0xffcdcdcd);
        }


        //最顶部通知开关
        //0xff585858 可点击 深颜色  0xffcdcdcd 不可点击 浅颜色
        section4_sw_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //不论通知开关是否打开，手动设置提醒时间的区域均为不可点击
                if (section4_sw_notifications.isChecked()){
                    editor.putBoolean("section4_sw_notifications", true);
                    editor.putBoolean("section4_sw_auto_noti", true);
                    editor.commit();

                    section4_sw_auto_noti.setChecked(true);
                    section4_tv_auto_noti.setTextColor(0xff585858);//颜色为可点击

                    section4_LL_Manual_noti.setClickable(false);
                    section4_tv_manual_noti.setTextColor(0xffcdcdcd);

                    //NotiService没有运行
                    if (!JudgeServiceIsRunning.isMyServiceRunning(getActivity())){
                        Intent intent = new Intent(getActivity(),NotiService.class);
                        getActivity().startService(intent);
                    }
                } else {
                    editor.putBoolean("section4_sw_notifications", false);
                    editor.putBoolean("section4_sw_auto_noti", false);
                    editor.commit();

                    section4_sw_auto_noti.setChecked(false);
                    section4_tv_auto_noti.setTextColor(0xffcdcdcd);//颜色为不可点击

                    section4_LL_Manual_noti.setClickable(false);
                    section4_tv_manual_noti.setTextColor(0xffcdcdcd);

                    Intent intent = new Intent(getActivity(),NotiService.class);
                    getActivity().stopService(intent);

                }
            }
        });

        //自动提醒开关
        //当自动提醒开启时，手动设置提醒时间设置为不可用状态，即不可点击且主提示文字改变为浅颜色
        section4_sw_auto_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section4_sw_auto_noti.isChecked()){
                    editor.putBoolean("section4_sw_auto_noti", true);
                    editor.commit();

                    section4_LL_Manual_noti.setClickable(false);
                    section4_tv_auto_noti.setTextColor(0xff585858);//自动提醒的文字改变为可用状态
                    section4_tv_manual_noti.setTextColor(0xffcdcdcd);//手动设置提醒的文字改变为不可用状态
                } else{
                    editor.putBoolean("section4_sw_auto_noti", false);
                    editor.commit();

                    section4_LL_Manual_noti.setClickable(true);
                    section4_tv_manual_noti.setTextColor(0xff585858);//手动设置提醒文字改变为可点击状态
                    section4_tv_auto_noti.setTextColor(0xffcdcdcd);//自动提醒的文字改变为不可点击状态
                }
            }
        });

        //手动设置提醒时间
        section4_LL_Manual_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sharedPreferences.getBoolean("section4_sw_notifications",true)){

                }else {
                    Fragment manualnoti = new ManualNotiFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, manualnoti)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }

}
