package com.cm.cmrl.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by Iddinesh.
 */
public class RetrofitClient {

    private static APIInterface restApiInterface;
    private static Retrofit retrofit;
    public static String BASE_URL = "http://cmrlcms.com/api/";
    // public static String BASE_URL = "http://cmrlcms.com/cmrl_dev/api/";
  //  public static String BASE_URL = "http://whitehousecbe.in/cmrlcms/api/";





    private static OkHttpClient buildClient() {
        return new OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    private static Gson gson = new GsonBuilder().setLenient().create();

    private static void createRetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(buildClient())
                .build();

    }

    public static APIInterface getApiService() {
        if (restApiInterface == null) {
            createRetrofitService();
            restApiInterface = retrofit.create(APIInterface.class);
        }
        return restApiInterface;
    }


}
