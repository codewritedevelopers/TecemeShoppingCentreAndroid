package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.form.LoginFormState;
import org.codewrite.teceme.model.form.ProfileFormState;
import org.codewrite.teceme.model.form.SignupFormState;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.rest.WishListJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.repository.CustomerRepository;
import org.codewrite.teceme.repository.WishListRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AccountViewModel
 */
public class WishListViewModel extends AndroidViewModel {

    private WishListRepository wishListRepository;

    public WishListViewModel(@NonNull Application application) {
        super(application);
        this.wishListRepository = new WishListRepository(application);
    }

    public void addWishList(Integer product_id, String owner,String accessToken) {
        final Call<WishListJson> wishList = wishListRepository.addToWishList(product_id, owner,accessToken);
        wishList.enqueue(new Callback<WishListJson>() {
            @Override
            public void onResponse(@NonNull Call<WishListJson> call,
                                   @NonNull Response<WishListJson> response) {
                if (response.isSuccessful()) {
                    wishListRepository.insert(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WishListJson> call, @NonNull Throwable t) {
                WishListJson result = new WishListJson();
                result.setStatus(false);
                if (t.getMessage() != null) {
                    result.setMessage(t.getMessage());
                } else {
                    result.setMessage("Process timeout! Please, Try again");
                }
            }
        });
    }

    public void removeWishList(final String id, String accessToken) {
        final Call<WishListJson> wishList = wishListRepository.removeWishList(id,accessToken);
        wishList.enqueue(new Callback<WishListJson>() {
            @Override
            public void onResponse(@NonNull Call<WishListJson> call,
                                   @NonNull Response<WishListJson> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        wishListRepository.delete(id);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WishListJson> call, @NonNull Throwable t) {
                WishListJson result = new WishListJson();
                result.setStatus(false);
                if (t.getMessage() != null) {
                    result.setMessage(t.getMessage());
                } else {
                    result.setMessage("Process timeout! Please, Try again");
                }
            }
        });
    }

    public void getWisLists(String accessToken) {
        final Call<List<WishListJson>> wishList = wishListRepository.getWishLists(accessToken);
        wishList.enqueue(new Callback<List<WishListJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<WishListJson>> call,
                                   @NonNull Response<List<WishListJson>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    int i = 0;
                    WishListEntity[] wishListEntities = new WishListEntity[response.body().size()];
                    for (WishListJson wishListJson : response.body()) {
                        wishListEntities[i++] = wishListJson;
                    }
                    wishListRepository.insert(wishListEntities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WishListJson>> call, @NonNull Throwable t) {
                WishListJson result = new WishListJson();
                result.setStatus(false);
                if (t.getMessage() != null) {
                    result.setMessage(t.getMessage());
                }
            }
        });
    }

    public LiveData<List<WishListEntity>> getWishListResult(String customer_id) {
        return wishListRepository.getWishListsResult(customer_id);
    }

    public LiveData<WishListEntity> isWishList(Integer product_id, String customer_id) {
        return wishListRepository.getWishList(product_id, customer_id);
    }
}
