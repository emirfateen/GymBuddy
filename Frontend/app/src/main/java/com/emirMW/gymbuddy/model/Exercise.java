package com.emirMW.gymbuddy.model;

import com.google.gson.annotations.SerializedName;

public class Exercise {
    @SerializedName("exeid")
    private String exeid;

    @SerializedName("name")
    private String name;

    @SerializedName("muscle")
    private String muscle;

    public String getExeId() {
        return exeid;
    }
    public void setExeId(String exeid){
        this.exeid=exeid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMuscle() {
        return muscle;
    }
    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }
}
