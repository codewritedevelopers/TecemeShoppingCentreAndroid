package org.codewrite.teceme.api;


import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.codewrite.teceme.R;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {

    private static Retrofit getRetrofitInstance(final Application application) {

        // create an HttpLogging Interceptor
       final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // create a gson object for json convector
        Gson gson = new GsonBuilder().setLenient().create();
        //create OkHttp Client
         OkHttpClient client = new OkHttpClient.Builder()
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

        return new Retrofit.Builder().baseUrl(application.getResources().getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public static RestApi getResetApi(Application application) {
        return getRetrofitInstance(application).create(RestApi.class);
    }

}
