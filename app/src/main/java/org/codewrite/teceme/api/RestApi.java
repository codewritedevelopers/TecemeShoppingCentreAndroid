package org.codewrite.teceme.api;

import org.codewrite.teceme.model.rest.CustomerJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestApi {

    /*
     * POST
     */
    @POST("customer/account")
    Call<CustomerJson> signup(@Body CustomerJson customer);

    @FormUrlEncoded
    @POST("customer/login")
    Call<CustomerJson> login(@Field("username") String username,
                             @Field("password") String password);

    @GET("customer")
    Call<CustomerJson> getCustomer();

}
