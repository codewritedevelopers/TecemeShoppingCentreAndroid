package org.codewrite.teceme.api;

import org.codewrite.teceme.model.rest.CategoryJson;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.rest.ProductJson;
import org.codewrite.teceme.model.rest.StoreJson;
import org.codewrite.teceme.model.rest.WalletJson;
import org.codewrite.teceme.model.rest.WalletLogJson;
import org.codewrite.teceme.model.rest.WishListJson;
import org.codewrite.teceme.model.room.WishListEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {

    /*
     * POST Customer
     */
    @POST("customers/account")
    Call<CustomerJson> signup(@Body CustomerJson customer);

    @FormUrlEncoded
    @POST("customers/login")
    Call<CustomerJson> login(@Field("username") String username,
                             @Field("password") String password);

    /*
     * PATCH Customer
     */
    @PATCH("customer/account/id/{id}/current_password/{current_password}")
    Call<CustomerJson> updateCustomer(@Body CustomerJson customerJson,
                                      @Path("id") String id,
                                      @Path("current_password") String currentPassword,
                                      @Header("Authorization") String token);

    @PATCH("customers/account/id/{id}")
    Call<CustomerJson> updateCustomer(@Body CustomerJson customerJson,
                                      @Path("id") String id,
                                      @Header("Authorization") String accessToken);

    /*
     * DELETE Customer
     */
    @DELETE("customers/account/id/{id}")
    Call<WalletJson> deleteCustomer(@Path("id") String id,
                                    @Header("Authorization") String token);

    /*
     * POST Wallet
     */
    @POST("wallets/wallet")
    Call<WalletJson> createWallet(@Body WalletJson walletJson,
                                  @Header("Authorization") String token);

    /*
     * POST Wallet
     */
    @POST("wallets/transfer-money")
    Call<WalletLogJson> transferMoney(@Body WalletLogJson walletLogJson,
                                      @Header("Authorization") String token);

    /*
     * POST Wallet
     */
    @POST("wallets/load-wallet")
    Call<WalletLogJson> loadWallet(@Body WalletLogJson walletLogJson,
                                   @Header("Authorization") String token);

    /*
     * PATCH Wallet
     */
    @PATCH("wallets/wallet")
    Call<WalletJson> updateWallet(@Body WalletJson walletJson,
                                  @Header("Authorization") String token);

    /*
     * DELETE Wallet
     */
    @DELETE("wallets/wallet/id/{id}")
    Call<WalletJson> deleteWallet(@Path("id") String id,
                                  @Header("Authorization") String token);

    /*
     * GET Category
     */
    @GET("categories/category")
    Call<List<CategoryJson>> getCategoryList(@Query("category_parent_id") Integer parent_id,
                                             @Query("category_level") Integer level);

    /*
     * GET Product
     */
    @GET("products/product")
    Call<List<ProductJson>> getProductList(@Query("product_category_id") Integer category_id,
                                           @Query("limit") Integer limit,
                                           @Query("page") Integer page);

    /*
     * GET Store
     */
    @GET("stores/store")
    Call<List<StoreJson>> getStoreList(@Query("store_category_id") Integer category_id,
                                       @Query("limit") int limit,
                                       @Query("page") Integer page);

    /*
     * POST WisList
     */
    @POST("customers/wish-list")
    Call<WishListJson> addToWishList(@Body WishListJson wishListJson,
                                     @Header("Authorization") String accessToken);

    /*
     * GET WishList
     */
    @GET("customers/wish-list")
    Call<List<WishListJson>> getWishList();

    @DELETE("customer/wish-list")
    Call<WishListJson> deleteWishList(@Body WishListJson wishListJson,
                                      @Header("Authorization") String accessToken);

    /*
     * GET Ads
     */
    @GET("customers/ads")
    Call<List<String>> getAds();

}
