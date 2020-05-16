package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.form.LoginFormState;
import org.codewrite.teceme.model.holder.Customer;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.repository.CustomerRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<CustomerJson> loginResult = new MutableLiveData<>();
    private CustomerRepository customerRepository;

    public CustomerViewModel(@NonNull Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<CustomerJson> getLoginResult() {
        return loginResult;
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


    public void loginFormDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
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
}
