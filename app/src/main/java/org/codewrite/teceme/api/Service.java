package org.codewrite.teceme.api;


import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.codewrite.teceme.R;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;

public class Service {

    private static Retrofit getRetrofitInstance(final Application application,
                                                final Map<String, String> additionalHeaders) {

        // create an HttpLogging Interceptor
       final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // create a gson object for json convector
        Gson gson = new GsonBuilder().setLenient().create();
        //create OkHttp Client
        OkHttpClient client;
        if (additionalHeaders==null) {
             client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            Request newRequest = originalRequest.newBuilder()
                                    .addHeader("X-API-KEY", application.getResources().getString(R.string.api_key))
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .addInterceptor(interceptor).build();
        }else{
             client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            Request newRequest = originalRequest.newBuilder()
                                    .addHeader("X-API-KEY", application.getResources().getString(R.string.api_key))
                                    .addHeader("ACCESS-TOKEN", Objects.requireNonNull(additionalHeaders.get("ACCESS_TOKEN")))
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .addInterceptor(interceptor).build();
        }
        return new Retrofit.Builder().baseUrl(application.getResources().getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public static RestApi getRestApi(Application application, Map<String,String> headers) {
        return getRetrofitInstance(application,headers).create(RestApi.class);
    }

}
