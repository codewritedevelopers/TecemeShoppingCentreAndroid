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

    public CheckOutViewModel(@NonNull Application application) {
        super(application);
        checkOutRepository = new CheckOutRepository(application);
    }

    public LiveData<List<CustomerOrderEntity>> getPendingOrders(String owner) {
        return checkOutRepository.getPendingOrders(owner);
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
            entity.setCustomer_product_code(cartEntity.getProduct_code());
            entity.setCustomer_order_product_img_uri(cartEntity.getProduct_img_uri());
            entity.setCustomer_order_product_category_id(cartEntity.getProduct_category_id());
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
            entity.setCustomer_product_code(cartEntity.getProduct_code());
            entity.setCustomer_order_product_img_uri(cartEntity.getProduct_img_uri());
            entity.setCustomer_order_product_category_id(cartEntity.getProduct_category_id());
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

    public LiveData<Result> getCheckOutResult() {
        return checkOutResult;
    }
}
