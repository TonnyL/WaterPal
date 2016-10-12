package com.marktony.drinkingwater;

/**
 * Created by lizhaotailang on 2015/11/18.
 */
public class NotiTime {

    private int Hour;
    private int Minute;

    public NotiTime(int hour,int minute){
        this.Hour = hour;
        this.Minute = minute;
    }

    public int getHour() {
        return Hour;
    }

    public int getMinute() {
        return Minute;
    }
}
