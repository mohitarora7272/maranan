package com.maranan.restclient;

import android.content.Context;

import com.maranan.utils.Config;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.client.OkClient;

public class RestClientRadioPrograms {
	private static RestClientRadioPrograms mContext;
	private static Api REST_CLIENT_RP;
	private static ExecutorService mExecutorService;

	// RestClientRadioPrograms Instance
	public static RestClientRadioPrograms getInstance() {
		return mContext;
	}
	
	public RestClientRadioPrograms(Context ctxx) {
		mContext = RestClientRadioPrograms.this;
		setupRestClient();
	}

	static {
		setupRestClient();
	}

	public static Api get() {
		return REST_CLIENT_RP;
	}

	private static void setupRestClient() {
		final OkHttpClient okHttpClient = new OkHttpClient();
		okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
		okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
		RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(
				Config.ROOT_SERVER_CLIENT).setClient(new OkClient(okHttpClient));
		builder.setLogLevel(RestAdapter.LogLevel.FULL);
		mExecutorService = Executors.newCachedThreadPool();
		builder.setExecutors(mExecutorService, new MainThreadExecutor());
		RestAdapter restAdapter = builder.build();
		REST_CLIENT_RP = restAdapter.create(Api.class);
	}

	public ExecutorService getExecuterService() {
		return mExecutorService;
	}
}
