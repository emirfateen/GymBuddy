package com.emirMW.gymbuddy.model;
import com.google.gson.annotations.SerializedName;
import java.util.UUID;
public class Routine {
    @SerializedName("rouid")
    private String rouid;
    @SerializedName("userid")
    private String userid;
    @SerializedName("rouname")
    private String rouname;
    @SerializedName("description")
    private String description;

    public String getRoutineId(){
        return rouid;
    }
    public void setRoutineId(String rouid){
        this.rouid = rouid;
    }
    public String getUserId(){
        return userid;
    }
    public void setUserId(String userid){
        this.userid = userid;
    }
    public String getRouName() {
        return rouname;
    }
    public void setRouName(String rouname) {
        this.rouname = rouname;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Routine{" +
                "rouid=" + rouid +
                ", userid=" + userid +
                ", rouname='" + rouname + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
