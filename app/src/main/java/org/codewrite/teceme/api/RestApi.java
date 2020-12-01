package org.codewrite.teceme.api;

import androidx.room.Delete;

import org.codewrite.teceme.model.rest.CategoryJson;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.rest.ProductJson;
import org.codewrite.teceme.model.rest.Result;
import org.codewrite.teceme.model.rest.StoreJson;
import org.codewrite.teceme.model.rest.StoreProductJson;
import org.codewrite.teceme.model.rest.WalletJson;
import org.codewrite.teceme.model.rest.WalletLogJson;
import org.codewrite.teceme.model.rest.WishListJson;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerOrderEntity;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.model.room.WishListEntity;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
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
    @PATCH("customers/account")
    Call<CustomerJson> updateCustomer(@Body CustomerJson customerJson,
                                      @Query("current_password") String currentPassword);

    @PATCH("customers/account")
    Call<CustomerJson> updateCustomer(@Body CustomerJson customerJson);

    /*
     * POST Wallet
     */
    @POST("wallets/wallet")
    Call<WalletJson> createWallet(@Body WalletJson walletJson);

    /*
     * POST Wallet
     */
    @POST("wallets/transfer-money")
    Call<WalletLogJson> transferMoney(@Body WalletLogJson walletLogJson);

    /*
     * POST Wallet
     */
    @POST("wallets/load-wallet")
    Call<WalletLogJson> loadWallet(@Body WalletLogJson walletLogJson);

    /*
     * PATCH Wallet
     */
    @PATCH("wallets/wallet")
    Call<WalletJson> updateWallet(@Body WalletJson walletJson);

    /*
     * DELETE Wallet
     */
    @DELETE("wallets/wallet/id/{id}")
    Call<WalletJson> deleteWallet(@Path("id") String id);

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

    @GET("products/product")
    Call<List<ProductJson>> getSearchProducts(@Query("query") String s);

    @GET("products/product/product_id/{id}")
    Call<ProductJson> getProduct(@Path("id") Integer product_id);

    /*
     * GET StoreProduct
     */
    @GET("stores/store-product/store_id/{store_id}")
    Call<List<StoreProductJson>> getStoreProducts(@Path("store_id") Integer store_id);
    /*
     * GET Store
     */
    @GET("stores/store")
    Call<List<StoreJson>> getStoreList();

    @GET("stores/store")
    Call<List<StoreJson>> getStoreListByCategory(@Query("store_category_id") Integer category_id);

    /*
     * POST WisList
     */
    @POST("customers/wish-list")
    Call<WishListJson> addToWishList(@Body WishListJson wishListJson);

    /*
     * GET WishList
     */
    @GET("customers/wish-list")
    Call<List<WishListJson>> getWishList();

    @DELETE("customers/wish-list/wishlist_id/{id}")
    Call<WishListJson> deleteWishList(@Path("id") String id);

    /*
     * GET Ads
     */
    @GET("customers/ads")
    Call<List<String>> getAds();


    @POST("customers/wallet-log")
    Call<WalletJson> addWalletLog(@Body WalletLogJson walletLogJson);

    @POST("customers/checkout/method/{method}")
    Call<Result> checkoutCustomer(@Body Map<String, Object> order,
                                  @Path("method") String method);

    @FormUrlEncoded
    @POST("customers/send-reset-link")
    Call<Result> resetPassword(@Field("username") String email);

    @GET("customers/orders/status/{status}")
    Call<List<CustomerOrderEntity>> getOrders(@Path("status") Integer status);

    @GET("customers/orders/id/{order_id}")
    Call<CustomerOrderEntity> getOrder(@Path("order_id") int order_id);


    @GET("stores/store-by-product/product_code/{product_code}")
    Call<List<StoreEntity>> getStoreListByProduct(@Path("product_code") String product_code);

    @GET("store-users/refresh-access-token")
    Call<Result> refreshAccessToken();
}
