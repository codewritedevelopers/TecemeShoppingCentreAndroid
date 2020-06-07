package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.form.LoginFormState;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.repository.CustomerRepository;
import org.codewrite.teceme.repository.WishListRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<CustomerJson> loginResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> wishListResult = new MutableLiveData<>();
    private CustomerRepository customerRepository;
    private MutableLiveData<List<String>> adsListResult= new MutableLiveData<>();

    public CustomerViewModel(@NonNull Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
    }

    public void login(String username, String password) {

        Call<CustomerJson> login = customerRepository.login(username, password);//*711*7#

        login.enqueue(new Callback<CustomerJson>() {
            @Override
            public void onResponse(@NonNull Call<CustomerJson> call,
                                   @NonNull Response<CustomerJson> response) {
                if(response.isSuccessful()) {
                    loginResult.postValue(response.body());
                   String token =  response.headers().get("Token");
                    Log.d("CustomerViewModel", "onResponse Token: "+token);
                }
                else {
                    loginResult.postValue(null);
                    String token =  response.headers().get("Token");
                    Log.d("CustomerViewModel", "onResponse: null "+token);
                }
            }
            @Override
            public void onFailure(@NonNull Call<CustomerJson> call, @NonNull Throwable t) {
                loginResult.postValue(null);
                Log.d("CustomerViewModel", "onResponse: null "+t.getMessage());
            }
        });
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }

    public LiveData<Boolean> isInWishList(String owner,int id) {
        customerRepository.getWishList(owner, id).observeForever(
                new Observer<WishListEntity>() {
            @Override
            public void onChanged(WishListEntity wishListEntity) {
                if (wishListEntity!=null) {
                    wishListResult.postValue(true);
                }else{
                    wishListResult.postValue(false);
                }
            }
        });
        return wishListResult;
    }

    public LiveData<List<String>> getAdsResult() {
        return adsListResult;
    }

    public void getAds() {
        Call<List<String>> ads = customerRepository.getAdds();
        ads.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call,
                                   @NonNull Response<List<String>> response) {
                if(response.isSuccessful()) {
                   adsListResult.postValue(response.body());
                }else{
                    adsListResult.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
            }
        });
    }

    public void stopFollowingStore(String store_id, String customer_id, String token) {

    }

    public void startFollowingStore(String store_id, String customer_id, String token) {

    }
}
