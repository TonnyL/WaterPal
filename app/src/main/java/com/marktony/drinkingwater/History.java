package com.marktony.drinkingwater;

/**
 * Created by lizhaotailang on 2015/9/27.
 * 操作用户的喝水记录
 */
public class History {

    private String timeDate,timeClock,timeWeek;
    private int bottle;

    public History(String timeDate,String timeClock,String week,int bottle){

        this.timeDate = timeDate;
        this.timeClock = timeClock;
        this.timeWeek = week;
        this.bottle = bottle;

    }

    public String getTimeClock() {
        return timeClock;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public String getTimeWeek() {
        return timeWeek;
    }

    public int getBottle() {
        return bottle;
    }

}
