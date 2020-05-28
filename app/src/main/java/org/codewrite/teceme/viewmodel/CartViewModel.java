package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.repository.CartRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends AndroidViewModel {

    private CartRepository cartRepository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
    }

    public LiveData<List<CartEntity>> getCartsEntity(String owner) {
        return cartRepository.getCarts(owner);
    }
    public void addToCart(CartEntity cartEntity){
        cartRepository.insert(cartEntity);
    }

    public LiveData<CartEntity> isInCart(Integer product_id) {
        return cartRepository.getCart(product_id);
    }

    public void removeFromCart(Integer product_id) {
        cartRepository.deleteCart(product_id);
    }
}
