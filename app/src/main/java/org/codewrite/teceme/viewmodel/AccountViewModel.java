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
import org.codewrite.teceme.model.rest.Result;
import org.codewrite.teceme.model.rest.WishListJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.WalletEntity;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.repository.CartRepository;
import org.codewrite.teceme.repository.CustomerRepository;
import org.codewrite.teceme.repository.WishListRepository;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AccountViewModel
 */
public class AccountViewModel extends AndroidViewModel {

    private boolean passwordMasked;
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<SignupFormState> signupFormState = new MutableLiveData<>();
    private MutableLiveData<ProfileFormState> profileFormState = new MutableLiveData<>();
    private MutableLiveData<CustomerJson> loginResult = new MutableLiveData<>();
    private MutableLiveData<CustomerJson> signupResult = new MutableLiveData<>();

    private CustomerRepository customerRepository;
    private LiveData<CustomerEntity> loggedInCustomer;
    private LiveData<AccessTokenEntity> accessToken;
    private MutableLiveData<CustomerJson> profileResult = new MutableLiveData<>();
    private MutableLiveData<CustomerJson> deleteResult = new MutableLiveData<>();
    private MutableLiveData<Result> resetPasswordResult =new MutableLiveData<>();

    public AccountViewModel(@NonNull Application application) {
        super(application);
        passwordMasked = true;
        customerRepository = new CustomerRepository(application);
        loggedInCustomer = customerRepository.getLoggedInCustomer();
        accessToken = customerRepository.getAccessToken();
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<SignupFormState> getSignupFormState() {
        return signupFormState;
    }

    public LiveData<ProfileFormState> getProfileFormState() {
        return profileFormState;
    }

    public LiveData<CustomerJson> getLoginResult() {
        return loginResult;
    }

    public LiveData<CustomerJson> getSignupResult() {
        return signupResult;
    }

    public MutableLiveData<CustomerJson> getProfileResult() {
        return profileResult;
    }

    public LiveData<CustomerEntity> getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void login(String username, String password) {

        final Call<CustomerJson> login = customerRepository.login(username, password);//*711*7#

        login.enqueue(new Callback<CustomerJson>() {
            @Override
            public void onResponse(@NonNull Call<CustomerJson> call,
                                   @NonNull Response<CustomerJson> response) {
                if (response.isSuccessful()) {
                    loginResult.postValue(response.body());
                    String token = response.headers().get("Token");
                    if (token != null) {
                        replaceWithAccessToken(new AccessTokenEntity(token));
                    }
                } else {
                    CustomerJson result = new CustomerJson();
                    result.setStatus(false);
                    result.setMessage(response.message());
                    loginResult.postValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerJson> call, @NonNull Throwable t) {
                CustomerJson result = new CustomerJson();
                result.setStatus(false);
                if(t.getMessage()!=null) {
                    result.setMessage(t.getMessage());
                }else{
                    result.setMessage("Process timeout! Please, Try again");
                }
                loginResult.postValue(result);
            }
        });
    }

    private void replaceWithAccessToken(AccessTokenEntity accessToken) {
        customerRepository.replaceAccessToken(accessToken);
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

    public void signUpFormDataChanged(String name, String phone, String username, String password, String cPassword) {
        if (!isNameValid(name)) {
            signupFormState.setValue(new SignupFormState(R.string.invalid_name,
                    null, null, null, null));
        }  else if (!isUserNameValid(username)) {
            signupFormState.setValue(new SignupFormState(null, null, R.string.invalid_username, null, null));
        } else if (!isPhoneValid(phone)) {
            signupFormState.setValue(new SignupFormState(null, R.string.invalid_phone, null, null, null));
        }else if (!isPasswordValid(password)) {
            signupFormState.setValue(new SignupFormState(null, null, null, R.string.invalid_password, null));
        } else if (!isConfirmPasswordValid(cPassword)) {
            signupFormState.setValue(new SignupFormState(null, null, null, null, R.string.invalid_password));
        } else if (!isConfirmPasswordValid2(password, cPassword)) {
            signupFormState.setValue(new SignupFormState(null, null, null, null, R.string.password_miss_match));
        } else {
            signupFormState.setValue(new SignupFormState(true));
        }
    }

    private boolean isConfirmPasswordValid(String cPassword) {
        return isPasswordValid(cPassword);
    }

    private boolean isPhoneValid(String phone) {
        return phone != null && Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isNameValid(String name) {
        return name != null && name.trim().length() > 0;
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        return username != null && Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.length() > 3;
    }

    private boolean isPasswordValidProfile(String password) {

        return password == null || password.length() > 3;
    }

    private boolean isConfirmPasswordValid2(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean isConfirmPasswordValidProfile(String password, String confirmPassword) {
        if (password == null) {
            return true;
        }
        return password.equals(confirmPassword);
    }

    public LiveData<AccessTokenEntity> getAccessToken() {
        return accessToken;
    }

    public void replaceWIthLoggedInCustomer(CustomerEntity customer) {
        customerRepository.replaceLoggedInCustomer(customer);
    }

    public boolean isPasswordMasked() {
        return passwordMasked;
    }

    public void setPasswordMasked(boolean passwordMasked) {
        this.passwordMasked = passwordMasked;
    }

    public void signup(String name, String phone, String username, String password, String cPassword) {
        Call<CustomerJson> signup = customerRepository.signup(name, phone, username, password);
        signup.enqueue(new Callback<CustomerJson>() {
            @Override
            public void onResponse(@NonNull Call<CustomerJson> call,
                                   @NonNull Response<CustomerJson> response) {
                if (response.isSuccessful()) {
                    signupResult.postValue(response.body());
                } else {
                    CustomerJson result = new CustomerJson();
                    result.setStatus(false);
                    result.setMessage(response.message());
                    signupResult.postValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerJson> call, @NonNull Throwable t) {
                CustomerJson customerJson = new CustomerJson();
                customerJson.setStatus(false);
                customerJson.setMessage(t.getMessage());
                signupResult.postValue(customerJson);
            }
        });
    }

    public void logoutCustomer() {
        customerRepository.logoutCustomer();
    }

    public void profileFormDataChanged(String name, String phone,
                                       String username, String password, String cPassword) {
        if (!isNameValid(name)) {
            profileFormState.setValue(new ProfileFormState(R.string.invalid_name,
                    null, null, null, null));
        } else if (!isPhoneValid(phone)) {
            profileFormState.setValue(new ProfileFormState(null, R.string.invalid_phone, null, null, null));
        } else if (!isUserNameValid(username)) {
            profileFormState.setValue(new ProfileFormState(null, null, R.string.invalid_username, null, null));
        } else if (!isPasswordValidProfile(password)) {
            profileFormState.setValue(new ProfileFormState(null, null, null, R.string.invalid_password, null));
        } else if (!isPasswordValidProfile(cPassword)) {
            profileFormState.setValue(new ProfileFormState(null, null, null, null, R.string.invalid_password));
        } else if (!isConfirmPasswordValidProfile(password, cPassword)) {
            profileFormState.setValue(new ProfileFormState(null, null, null, null, R.string.password_miss_match));
        } else {
            profileFormState.setValue(new ProfileFormState(true));
        }
    }

    public void update(String name, String phone, String username, String password,
                       String currentPassword, String id, String accessToken) {
        Call<CustomerJson> customerJsonCall
                = customerRepository.updateCustomer(name, phone, username, password, currentPassword, id, accessToken);
        customerJsonCall.enqueue(new Callback<CustomerJson>() {
            @Override
            public void onResponse(@NonNull Call<CustomerJson> call,
                                   @NonNull Response<CustomerJson> response) {
                if (response.isSuccessful()) {
                    profileResult.postValue(response.body());
                    String token = response.headers().get("Token");
                    if (token != null) {
                        updateWithAccessToken(new AccessTokenEntity(token));
                    }
                } else {
                    CustomerJson result = new CustomerJson();
                    result.setStatus(false);
                    result.setMessage(response.message());
                    profileResult.postValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerJson> call, @NonNull Throwable t) {
                CustomerJson customerJson = new CustomerJson();
                customerJson.setStatus(false);
                customerJson.setMessage(t.getMessage());
                signupResult.postValue(customerJson);
            }
        });
    }

    private void updateWithAccessToken(AccessTokenEntity accessTokenEntity) {
        customerRepository.replaceAccessToken(accessTokenEntity);
    }

    public void deleteAccount(String id) {
        final Call<CustomerJson> delete = customerRepository.delete(id);//*711*7#

        delete.enqueue(new Callback<CustomerJson>() {
            @Override
            public void onResponse(@NonNull Call<CustomerJson> call,
                                   @NonNull Response<CustomerJson> response) {
                if (response.isSuccessful()) {
                    logoutCustomer();
                    clearAccessToken();;
                } else{
                    CustomerJson result = new CustomerJson();
                    result.setStatus(false);
                    result.setMessage(response.message());
                    deleteResult.postValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerJson> call, @NonNull Throwable t) {
                CustomerJson result = new CustomerJson();
                result.setStatus(false);
                if(t.getMessage()!=null) {
                    result.setMessage(t.getMessage());
                }else{
                    result.setMessage("Process timeout! Please, Try again");
                }
                deleteResult.postValue(result);
            }
        });
    }

    private void clearAccessToken() {
        customerRepository.deleteAllAccessToken();
    }

    public void stopFollowingStore(String store_id, String customer_id, String token) {


    }

    public void startFollowingStore(String store_id, String customer_id, String token) {

    }

    public void resetPassword(String email) {
        Call<Result> resultCall = customerRepository.resetPassword(email);
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call,
                                   @NonNull Response<Result> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    resetPasswordResult.postValue(response.body());
                }else{
                    Result result = new Result();
                    result.setStatus(false);
                    result.setMessage(response.message());
                    resetPasswordResult.postValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                Result result = new Result();
                result.setStatus(false);
                result.setMessage(t.getMessage());
                resetPasswordResult.postValue(result);
            }
        });
    }

    public LiveData<Result> getResetPasswordResult() {
        return resetPasswordResult;
    }
}
