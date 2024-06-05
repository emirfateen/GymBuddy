package com.emirMW.gymbuddy.request;

import com.emirMW.gymbuddy.model.Exercise;
import com.emirMW.gymbuddy.model.LoginResponse;
import com.emirMW.gymbuddy.model.Reminder;
import com.emirMW.gymbuddy.model.Routine;
import com.emirMW.gymbuddy.model.User;
import com.emirMW.gymbuddy.model.Variation;

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    //USER
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user/signup")
    Call<User> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("user/logout")
    Call<Void> logout();

    //EXERCISE
    @GET("exercise/getExercise")
    Call<ArrayList<Exercise>> getExercise();
    @GET("exercise/getById/{exeid}")
    Call<Exercise> getExerciseById(
            @Path("exeid") String exeId
    );

    //ROUTINE
    @GET("routine/getAll")
    Call<ArrayList<Routine>> getAllRou(
            @Query("userid") String userid
    );
    @FormUrlEncoded
    @POST("routine/create")
    Call<Routine> createRoutine(
            @Field("userid") String userid,
            @Field("rouname") String rouname,
            @Field("description") String description
    );
    @DELETE("routine/delete/{rouid}")
    Call<Void> deleteRoutine (
            @Path("rouid") String rouid
    );

    //VARIATION
    @GET("variation/getAll/{rouid}")
    Call<ArrayList<Variation>> getAllVar(
            @Path("rouid") String rouid
    );
    @FormUrlEncoded
    @POST("variation/create")
    Call<Variation> createVariation(
            @Field("exeid") String exeid,
            @Field("rouid") String rouid,
            @Field("sets") String sets,
            @Field("reps") String reps
    );
    @GET("variation/getById/{varid}")
    Call<Variation> getDetailVariation (
            @Path("varid") String varid
    );
    @DELETE("variation/delete/{varid}")
    Call<Void> deleteVariation (
            @Path("varid") String varid
    );
    @FormUrlEncoded
    @PUT("variation/update/{varid}")
    Call<Variation> updateVariation (
            @Path("varid") String varid,
            @Field("sets") String sets,
            @Field("reps") String reps
    );

    //REMINDER
    @GET("reminder/getAll")
    Call<ArrayList<Reminder>> getAllRem(
            @Query("userid") String userid
    );
    @FormUrlEncoded
    @POST("reminder/create")
    Call<Reminder> createReminder(
            @Field("userid") String userid,
            @Field("remname") String remname,
            @Field("reminder_time") String reminder_time
    );
    @GET("reminder/getById/{remid}")
    Call<Reminder> getDetailReminder (
            @Path("remid") String remid
    );
    @DELETE("reminder/delete/{remid}")
    Call<Void> deleteReminder (
            @Path("remid") String remid
    );

    @FormUrlEncoded
    @PUT("reminder/update/{remid}")
    Call<Reminder> updateReminder (
            @Path("remid") String remid,
            @Field("remname") String remname,
            @Field("reminder_time") String reminder_time
    );
}
