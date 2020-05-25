package org.codewrite.teceme.api;

import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.rest.WalletJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @PATCH("customer/account/{id}/{current_password}")
    Call<CustomerJson> updateCustomer(@Body CustomerJson customerJson,
                                      @Path("id") String id,
                                      @Path("current_password") String currentPassword,
                                      @Header("Authorization") String token);

    @POST("customer/wallet")
    Call<WalletJson> createWallet(@Body WalletJson walletJson,
                                  @Header("Authorization") String token);

    @PATCH("customer/wallet")
    Call<WalletJson> updateWallet(@Body WalletJson walletJson,
                                  @Header("Authorization") String token);

    @PATCH("customer/account/{id}")
    Call<CustomerJson> updateCustomer(@Body CustomerJson customerJson,
                                      @Path("id") String id,
                                      @Header("Authorization") String accessToken);

}
