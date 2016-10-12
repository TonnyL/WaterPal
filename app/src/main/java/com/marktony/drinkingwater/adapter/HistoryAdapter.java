package com.marktony.drinkingwater.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marktony.drinkingwater.History;
import com.marktony.drinkingwater.R;

import java.util.List;

/**
 * Created by lizhaotailang on 2015/9/27.
 * 自定义适配器，泛型为Histoty
 */
public class HistoryAdapter extends ArrayAdapter<History> {

    private int resourceId;

    public HistoryAdapter(Context context, int resource,List<History> objects) {

        super(context, resource,objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        History history = getItem(position);//获取当前的History实例
        View view;//使用LayoutInflater来为子项加载传入的布局
        ViewHolder viewHolder;


        /**
         * 将之前加载好的布局进行缓存，以便重新启用
         * 在getview()方法进行判断，如果convertView为空，则使用LayoutInflater加载布局
         * 如果不为空则直接对convertView进行重用
         * 当convertView为空时，创建一个ViewHolder对象，并将控件的实例都存放在ViewHolder中
         * 然后调用View的setTag()方法，把ViewHolder重新取出
         */
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.textView_timedate = (TextView) view.findViewById(R.id.history_time_date);
            viewHolder.textView_timeweek = (TextView) view.findViewById(R.id.history_week);
            viewHolder.textView_histoty_bottle = (TextView) view.findViewById(R.id.history_bottle);
            viewHolder.textView_timeclock = (TextView) view.findViewById(R.id.history_time_clock);
            view.setTag(viewHolder);//将ViewHolder存储在View中
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
        }
        viewHolder.textView_timedate.setText(getContext().getString(R.string.history_adapter_date) + history.getTimeDate());
        viewHolder.textView_timeweek.setText( "" + history.getTimeWeek());
        viewHolder.textView_timeclock.setText(getContext().getString(R.string.history_adapter_clock) + history.getTimeClock());
        viewHolder.textView_histoty_bottle.setText(getContext().getString(R.string.history_adapter_bottle_part1)
                + String.valueOf(history.getBottle())
                + getContext().getString(R.string.history_adapter_bottle_part2) );
        return view;
    }

    //内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView textView_timedate;
        TextView textView_timeweek;
        TextView textView_timeclock;
        TextView textView_histoty_bottle;
    }

}
