package com.marktony.drinkingwater.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.marktony.drinkingwater.History;
import com.marktony.drinkingwater.adapter.HistoryAdapter;
import com.marktony.drinkingwater.MyDatabaseHelper;
import com.marktony.drinkingwater.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 关联内容为喝水记录
 */
public class MainFragment_section2 extends Fragment {

    private View view;
    private TextView textView;

    public MainFragment_section2() {
        // 空的构造函数
    }

    private List<History> historyList = new ArrayList<History>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //此处加载布局是一定要带第三个参数false，否则加载出错
        view = inflater.inflate(R.layout.fragment_main_fragment_section2,container,false);

        final MyDatabaseHelper helper = new MyDatabaseHelper(getActivity(),"History.db",null,1);

        textView = (TextView) view.findViewById(R.id.section2_tv);
        ListView listView = (ListView) view.findViewById(R.id.section2_list);

        //初始化
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("History",null,null,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                History h = new History(
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("week")),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("bottle"))));
                historyList.add(h);
            }while (cursor.moveToNext());
        }
        cursor.close();

        if (historyList.isEmpty()){
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }else{
            HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(),R.layout.history_item,historyList);
            listView.setAdapter(historyAdapter);
        }

        return view;
    }


}
