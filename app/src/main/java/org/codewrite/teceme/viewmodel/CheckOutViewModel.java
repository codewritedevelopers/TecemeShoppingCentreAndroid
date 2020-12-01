package org.codewrite.teceme.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.codewrite.teceme.model.rest.CardMethodJson;
import org.codewrite.teceme.model.rest.MobileMethodJson;
import org.codewrite.teceme.model.rest.Result;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerOrderEntity;
import org.codewrite.teceme.repository.CheckOutRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutViewModel extends AndroidViewModel {

    private CheckOutRepository checkOutRepository;
    private MutableLiveData<Result> checkOutResult = new MutableLiveData<>();
    private MutableLiveData<List<CustomerOrderEntity>> orderResult = new MutableLiveData<>();
    private MutableLiveData<List<CustomerOrderEntity>> orderPendingResult = new MutableLiveData<>();
    private MutableLiveData<List<CustomerOrderEntity>> orderRedeemedResult = new MutableLiveData<>();

    public CheckOutViewModel(@NonNull Application application) {
        super(application);
        checkOutRepository = new CheckOutRepository(application);
    }

    public void getOrders(String token,Integer status) {
        Call<List<CustomerOrderEntity>> listCall = checkOutRepository.getOrders(token,status);
        listCall.enqueue(new Callback<List<CustomerOrderEntity>>() {
            @Override
            public void onResponse(@NonNull Call<List<CustomerOrderEntity>> call,
                                   @NonNull Response<List<CustomerOrderEntity>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    orderResult.postValue(response.body());
                }else{
                    orderResult.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CustomerOrderEntity>> call, @NonNull Throwable t) {
               orderResult.postValue(null);
            }
        });
    }

    public void getRedeemedOrders(String token) {
        Call<List<CustomerOrderEntity>> listCall = checkOutRepository.getOrders(token,1);
        listCall.enqueue(new Callback<List<CustomerOrderEntity>>() {
            @Override
            public void onResponse(@NonNull Call<List<CustomerOrderEntity>> call,
                                   @NonNull Response<List<CustomerOrderEntity>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    orderRedeemedResult.postValue(response.body());
                }else{
                    orderRedeemedResult.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CustomerOrderEntity>> call, @NonNull Throwable t) {
                orderRedeemedResult.postValue(null);
            }
        });
    }

    public void getPendingOrders(String token) {
        Call<List<CustomerOrderEntity>> listCall = checkOutRepository.getOrders(token,0);
        listCall.enqueue(new Callback<List<CustomerOrderEntity>>() {
            @Override
            public void onResponse(@NonNull Call<List<CustomerOrderEntity>> call,
                                   @NonNull Response<List<CustomerOrderEntity>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    orderPendingResult.postValue(response.body());
                }else{
                    orderPendingResult.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CustomerOrderEntity>> call, @NonNull Throwable t) {
                orderPendingResult.postValue(null);
            }
        });
    }

    public void checkoutCustomer(List<CartEntity> cartEntities, MobileMethodJson method, String token) {
        List<CustomerOrderEntity> orderEntities = new ArrayList<>();
        for (CartEntity cartEntity : cartEntities) {
            CustomerOrderEntity entity = new CustomerOrderEntity();
            entity.setCustomer_order_customer_id(cartEntity.getCart_owner());
            entity.setCustomer_order_product_color(cartEntity.getProduct_color());
            entity.setCustomer_order_product_size(cartEntity.getProduct_size());
            entity.setCustomer_order_quantity(cartEntity.getCart_quantity());
            entity.setCustomer_order_product_price(cartEntity.getProduct_price());
            entity.setCustomer_order_product_weight(cartEntity.getProduct_weight());
            entity.setCustomer_order_product_code(cartEntity.getProduct_code());
            entity.setCustomer_order_product_img_uri(cartEntity.getProduct_img_uri());
            entity.setCustomer_order_product_name(cartEntity.getProduct_name());
            entity.setCustomer_order_product_desc(cartEntity.getProduct_desc());
            //  add
            orderEntities.add(entity);
        }
        Map<String, Object> order = new HashMap<>();
        order.put("mobile", method);
        order.put("order", orderEntities);

        Call<Result> listCall = checkOutRepository.checkoutCustomer(order,"mobile", token);
        listCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call,
                                   @NonNull Response<Result> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    checkOutResult.postValue(response.body());
                }else{
                    Result result = new Result();
                    result.setStatus(false);
                    result.setMessage(response.message());
                    checkOutResult.postValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                Result result = new Result();
                result.setStatus(false);
                result.setMessage(t.getMessage());
                checkOutResult.postValue(result);
            }
        });
    }


    public void checkoutCustomer(List<CartEntity> cartEntities, CardMethodJson method, String token) {
        List<CustomerOrderEntity> orderEntities = new ArrayList<>();
        for (CartEntity cartEntity : cartEntities) {
            CustomerOrderEntity entity = new CustomerOrderEntity();
            entity.setCustomer_order_customer_id(cartEntity.getCart_owner());
            entity.setCustomer_order_product_color(cartEntity.getProduct_color());
            entity.setCustomer_order_product_size(cartEntity.getProduct_size());
            entity.setCustomer_order_quantity(cartEntity.getCart_quantity());
            entity.setCustomer_order_product_price(cartEntity.getProduct_price());
            entity.setCustomer_order_product_weight(cartEntity.getProduct_weight());
            entity.setCustomer_order_product_code(cartEntity.getProduct_code());
            entity.setCustomer_order_product_img_uri(cartEntity.getProduct_img_uri());
            entity.setCustomer_order_product_name(cartEntity.getProduct_name());
            entity.setCustomer_order_product_desc(cartEntity.getProduct_desc());
            //  add
            orderEntities.add(entity);
        }
        Map<String, Object> order = new HashMap<>();
        order.put("card", method);
        order.put("order", orderEntities);

        Call<Result> listCall = checkOutRepository.checkoutCustomer(order,"card", token);
        listCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call,
                                   @NonNull Response<Result> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    checkOutResult.setValue(response.body());
                }else{
                    Result result = new Result();
                    result.setStatus(false);
                    result.setMessage(response.message());
                    checkOutResult.setValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                Result result = new Result();
                result.setStatus(false);
                result.setMessage(t.getMessage());
                checkOutResult.setValue(result);
            }
        });
    }

    public LiveData<Result> getCheckOutResult() {
        return checkOutResult;
    }

    public LiveData<List<CustomerOrderEntity>> getOrdersResult() {
        return orderResult;
    }

    public LiveData<List<CustomerOrderEntity>> getOrderRedeemedResult() {
        return orderRedeemedResult;
    }

    public LiveData<List<CustomerOrderEntity>> getOrderPendingResult() {
        return orderPendingResult;
    }

    public LiveData<CustomerOrderEntity> getOrderResult(String token, int order_id) {
        final MutableLiveData<CustomerOrderEntity> liveData = new MutableLiveData<>();

        Call<CustomerOrderEntity> listCall = checkOutRepository.getOrder( token, order_id);
        listCall.enqueue(new Callback<CustomerOrderEntity>() {
            @Override
            public void onResponse(@NonNull Call<CustomerOrderEntity> call,
                                   @NonNull Response<CustomerOrderEntity> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    liveData.postValue(response.body());
                }else{
                    CustomerOrderEntity result = new CustomerOrderEntity();
                    result.setStatus(false);
                    result.setMessage(response.message());
                    liveData.postValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerOrderEntity> call, @NonNull Throwable t) {
                CustomerOrderEntity result = new CustomerOrderEntity();
                result.setStatus(false);
                result.setMessage(t.getMessage());
                checkOutResult.postValue(result);
            }
        });
        return liveData;
    }
}
