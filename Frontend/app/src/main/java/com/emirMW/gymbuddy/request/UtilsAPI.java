package com.emirMW.gymbuddy.request;

public class UtilsAPI {
    public static final String BASE_URL_API = "http://192.168.18.35:3000/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
