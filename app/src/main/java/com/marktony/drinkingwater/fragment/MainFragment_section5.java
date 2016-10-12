package com.marktony.drinkingwater.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.marktony.drinkingwater.activities.MainActivity;
import com.marktony.drinkingwater.R;

import java.util.Locale;


/**
 * 关联内容为应用设置
 */
public class MainFragment_section5 extends Fragment {

    private View view;
    private com.rey.material.widget.Switch section5_sw_congratulate;
    private com.rey.material.widget.Switch section5_sw_auto_update;
    private LinearLayout section5_LL_launguage;
    private LinearLayout section5_LL_report;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private TextView section5_tv_language_small;


    public MainFragment_section5() {

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main_fragment_section5,container,false);

        section5_sw_congratulate = (com.rey.material.widget.Switch) view.findViewById(R.id.section5_sw_noti_congratulate);
        section5_sw_auto_update = (com.rey.material.widget.Switch) view.findViewById(R.id.section5_sw_auto_update);
        section5_LL_launguage = (LinearLayout) view.findViewById(R.id.section5_LL_language);
        section5_tv_language_small = (TextView) view.findViewById(R.id.section5_tv_language_small);
        section5_LL_report = (LinearLayout) view.findViewById(R.id.section5_LL_report);

        sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE).edit();

        section5_sw_congratulate.setChecked(sharedPreferences.getBoolean("Congratulate", true));
        section5_sw_auto_update.setChecked(sharedPreferences.getBoolean("Auto_update", true));

        section5_sw_congratulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section5_sw_congratulate.isChecked() == true) {
                    editor.putBoolean("Congratulate", true);
                    editor.commit();
                } else {
                    editor.putBoolean("Congratulate", false);
                    editor.commit();
                }
            }
        });


        section5_sw_auto_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section5_sw_auto_update.isChecked() == true){
                    editor.putBoolean("Auto_update", true);
                    editor.commit();
                }else{
                    editor.putBoolean("Auto_update",false);
                    editor.commit();
                }
            }
        });

        /**
         * 此处为语言切换，还需要优化的有
         * 代码的重复以及优化
         * 此处只实现了两种语言的切换问题，但是当有多种语言时，切换就会非常麻烦，需要修改
         */

        final Locale locale = getResources().getConfiguration().locale;
        final String language = locale.getLanguage();

        //设置切换语言选项下的提示语句
        if(language.endsWith("zh")){
            section5_tv_language_small.setText(R.string.language_ZH);
        }else {
            section5_tv_language_small.setText(R.string.language_En);
        }

        section5_LL_launguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Resources resources = getResources();
                final Configuration config = resources.getConfiguration();

                View language_view = inflater.inflate(R.layout.language, null);

                final RadioButton rbZH, rbEn;
                rbZH = (RadioButton) language_view.findViewById(R.id.language_rbZH);
                rbEn = (RadioButton) language_view.findViewById(R.id.language_rbEn);

                //获取当前系统语言
                if (language.endsWith("zh")) {
                    rbZH.setChecked(true);
                    rbEn.setChecked(false);
                } else {
                    rbZH.setChecked(false);
                    rbEn.setChecked(true);
                }

                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle(R.string.section5_tv_language);
                dialog.setCancelable(true);
                dialog.setContentView(language_view);
                dialog.show();


                rbZH.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rbZH.setChecked(true);
                        rbEn.setChecked(false);
                        section5_tv_language_small.setText(R.string.language_ZH);
                        config.locale = Locale.CHINESE;
                        resources.updateConfiguration(config,null);
                        getActivity().finish();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                        dialog.dismiss();
                    }
                });

                rbEn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rbEn.setChecked(true);
                        rbZH.setChecked(false);
                        section5_tv_language_small.setText(R.string.language_En);
                        config.locale = Locale.ENGLISH;
                        resources.updateConfiguration(config, null);
                        getActivity().finish();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        });

        section5_LL_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getString(R.string.section6_mail_uri));
                String[] email = {getString(R.string.section6_mail_cc)};
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.section5_tv_report_mail_subject)); // 主题
                startActivity(Intent.createChooser(intent,getString(R.string.section6_mail_chooser)));
            }
        });

        return view;
    }

}
