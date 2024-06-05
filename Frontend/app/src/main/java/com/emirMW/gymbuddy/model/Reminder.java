package com.emirMW.gymbuddy.model;

import com.google.gson.annotations.SerializedName;

public class Reminder {
    @SerializedName("remid")
    private String remid;
    @SerializedName("userid")
    private String userid;
    @SerializedName("remname")
    private String remname;
    @SerializedName("reminder_time")
    private String reminder_time;

    public String getReminderId(){
        return remid;
    }
    public void setReminderId(String remid){
        this.remid = remid;
    }
    public String getUserId(){
        return userid;
    }
    public void setUserId(String userid){
        this.userid = userid;
    }
    public String getRemName() {
        return remname;
    }
    public void setRemName(String remname) {
        this.remname = remname;
    }
    public String getReminder_time() {
        return reminder_time;
    }
    public void setReminder_time(String reminder_time) {
        this.reminder_time = reminder_time;
    }

    @Override
    public String toString() {
        return "Routine{" +
                "rouid=" + remid +
                ", userid=" + userid +
                ", remname='" + remname + '\'' +
                ", reminder_time='" + reminder_time + '\'' +
                '}';
    }
}
