package com.example.baeminsu.nodechat.HTTP;

import com.example.baeminsu.nodechat.NetworkHelper.NetworkDefine;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by baeminsu on 2018. 5. 24..
 */

public class RetrofitClient {

    private static final String baseURL = NetworkDefine.BASE_URL;
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .connectTimeout(2000,TimeUnit.MILLISECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;

    }

}
