package com.suntelecoms.dynamicform.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUser {

    private static Retrofit retrofit = null;

    public static final String BASE_URL = "https://seedofafrica.com/api/app/mobile/";


public static MyApi getRoutes(){

    OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            if(retrofit==null){
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                retrofit = new Retrofit
                        .Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build();
            }



    return retrofit.create(MyApi.class);
}

}
