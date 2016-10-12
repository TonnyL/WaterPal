package com.marktony.drinkingwater.fragment;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.marktony.drinkingwater.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;


/**
 *关联内容为关于我们
 */
public class MainFragment_section6 extends Fragment {

    private View view;
    private LinearLayout LL_feedback;
    private LinearLayout LL_sharetofriends;
    private LinearLayout LL_score;
    private LinearLayout LL_update;
    private LinearLayout LL_version;


    public MainFragment_section6() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_main_fragment_section6, container, false);

        LL_feedback = (LinearLayout) view.findViewById(R.id.section6_LL_feedback);
        LL_score = (LinearLayout) view.findViewById(R.id.section6_LL_score);
        LL_sharetofriends = (LinearLayout) view.findViewById(R.id.section6_LL_sharetofriends);
        LL_update = (LinearLayout) view.findViewById(R.id.section6_LL_update);
        LL_version = (LinearLayout) view.findViewById(R.id.section6_LL_version);

        LL_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id="+getActivity().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        LL_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getString(R.string.section6_mail_uri));
                String[] email = {getString(R.string.section6_mail_cc)};
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.section6_mail_subject)); // 主题
                intent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.section6_mail_text1)//手机型号
                        + Build.MODEL + "\n"
                        + getString(R.string.section6_mail_text2)//SDK版本
                        + Build.VERSION.SDK_INT + "\n"
                        + getString(R.string.section6_mail_text3)// 系统版本
                        + Build.VERSION.RELEASE + "\n");
                startActivity(Intent.createChooser(intent,getString(R.string.section6_mail_chooser)));
            }
        });

        LL_sharetofriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.section6_share_text));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getText(R.string.section6_share_title)));
            }
        });

        LL_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUpdateAgent.forceUpdate(getActivity());
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                        if (i == UpdateStatus.No) {
                            Toast.makeText(getActivity(), R.string.section6_toast_updateResponse_NO, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        LL_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final com.rey.material.app.Dialog dialog = new com.rey.material.app.Dialog(getActivity());
                dialog.setTitle(R.string.section6_version_dialog_title);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.thanks_dialog);
                dialog.neutralAction(R.string.section6_version_dialog_btn);
                dialog.neutralActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

}
