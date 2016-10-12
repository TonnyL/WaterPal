package com.marktony.drinkingwater.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marktony.drinkingwater.NotiTime;
import com.marktony.drinkingwater.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by lizhaotailang on 2015/11/18.
 */
public class NotiTimeAdapter extends ArrayAdapter<NotiTime> {

    private int resourceId;
    public NotiTimeAdapter(Context context, int resource, List<NotiTime> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NotiTime notiTime = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_hour = (TextView) view.findViewById(R.id.notitime_hour);
            viewHolder.tv_minute = (TextView) view.findViewById(R.id.notitime_minute);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_hour.setText(String.valueOf(notiTime.getHour()));
        viewHolder.tv_minute.setText(String.valueOf(notiTime.getMinute()));

        return view;
    }

    class ViewHolder{
        TextView tv_hour;
        TextView tv_minute;
    }
}
