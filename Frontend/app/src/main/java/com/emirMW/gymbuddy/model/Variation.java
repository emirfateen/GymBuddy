package com.emirMW.gymbuddy.model;

import com.google.gson.annotations.SerializedName;

public class Variation {
    @SerializedName("varid")
    private String varid;

    @SerializedName("exeid")
    private String exeid;

    @SerializedName("rouid")
    private String rouid;

    @SerializedName("sets")
    private String sets;

    @SerializedName("reps")
    private String reps;

    public String getVarId(){
        return varid;
    }
    public void setVarId(String varid){
        this.varid = varid;
    }
    public String getExeId(){
        return exeid;
    }
    public void setExeId(String exeid){
        this.exeid = exeid;
    }
    public String getRoutineId(){
        return rouid;
    }
    public void setRoutineId(String rouid){
        this.rouid = rouid;
    }
    public String getSets(){
        return sets;
    }
    public void setSets(String sets){
        this.sets = sets;
    }
    public String getReps(){ return reps; }
    public void setReps(String reps){
        this.reps = reps;
    }
}
