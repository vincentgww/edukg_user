package com.fairychild.edukguser;

import android.content.Context;

import java.io.IOException;

import okhttp3.*;

public class OkHttp {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public static String post(String url, String json, Context context) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new NoConnectionInterceptor(context))
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String get(String url, Context context) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new NoConnectionInterceptor(context))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


}
