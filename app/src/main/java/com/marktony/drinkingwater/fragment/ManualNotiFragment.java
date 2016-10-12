package com.marktony.drinkingwater.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.marktony.drinkingwater.NotiTime;
import com.marktony.drinkingwater.R;
import com.marktony.drinkingwater.adapter.NotiTimeAdapter;

import java.util.ArrayList;
import java.util.List;

//关联手动设置提醒
public class ManualNotiFragment extends Fragment{

    private View view;
    private FloatingActionButton fab;
    private ListView noti_list;
    private List<NotiTime> notiTimeList = new ArrayList<NotiTime>();


    public ManualNotiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view  = inflater.inflate(R.layout.fragment_manual_noti,container,false);

        noti_list = (ListView) view.findViewById(R.id.manua_noti_time_list);
        fab = (FloatingActionButton) view.findViewById(R.id.manul_noti_add);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Calendar c = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
                timePickerDialog.setCancelable(true);
                timePickerDialog.show();*/
                Snackbar.make(v, R.string.manualnoti_snack, Snackbar.LENGTH_SHORT).show();
            }
        });

        NotiTime nt = new NotiTime(10,20);
        notiTimeList.add(nt);
        NotiTimeAdapter notiTimeAdapter = new NotiTimeAdapter(getActivity(),R.layout.notitime_item,notiTimeList);
        noti_list.setAdapter(notiTimeAdapter);
        return view;
    }

}
