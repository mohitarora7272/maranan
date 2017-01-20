package com.maranan.restclient;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.client.OkClient;

public class RestClient {
    private static RestClient mContext;
    private static Api REST_CLIENT;
    private static String ROOT = "https://www.googleapis.com/youtube/v3";
    private static ExecutorService mExecutorService;

    // RestClient Instance
    public static RestClient getInstance() {
        return mContext;
    }

    public RestClient(Context ctxx) {
        mContext = RestClient.this;
        setupRestClient();
    }

    static {
        setupRestClient();
    }

    public static Api get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(
                ROOT).setClient(new OkClient(okHttpClient));
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        mExecutorService = Executors.newCachedThreadPool();
        builder.setExecutors(mExecutorService, new MainThreadExecutor());
        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(Api.class);
    }

    public ExecutorService getExecuterService() {
        return mExecutorService;
    }
}
