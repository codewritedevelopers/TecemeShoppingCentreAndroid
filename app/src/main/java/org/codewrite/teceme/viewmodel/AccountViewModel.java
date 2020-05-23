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
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.repository.CustomerRepository;

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
    private MutableLiveData<CustomerJson> profileUpDateResult = new MutableLiveData<>();

    private CustomerRepository customerRepository;
    private LiveData<CustomerEntity> loggedInCustomer;
    private LiveData<AccessTokenEntity> accessToken;

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

    public LiveData<CustomerJson> getProfileUpdateResult() {
        return profileUpDateResult;
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
                if(response.isSuccessful()) {
                    loginResult.postValue(response.body());
                   String token =  response.headers().get("Token");
                   replaceWithAccessToken( new AccessTokenEntity(token));
                }
                else {
                    loginResult.postValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<CustomerJson> call, @NonNull Throwable t) {
                CustomerJson result = new CustomerJson();
                result.setStatus(false);
                result.setMessage(t.getMessage());
                loginResult.postValue(result);
            }
        });
    }

    private void replaceWithAccessToken(AccessTokenEntity accessToken){
        customerRepository.replaceAccessToken(accessToken);
    }
    private void updateAccessToken(AccessTokenEntity accessTokenEntity){
        customerRepository.updateAccessToken(accessTokenEntity);
    }

    public void loginFormDataChanged(String username, String password) {
        if (isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void signUpFormDataChanged(String name, String phone,String username, String password, String cPassword) {
        if (!isNameValid(name)) {
            signupFormState.setValue(new SignupFormState(R.string.invalid_name,
                    null,null,null,null));
        }
        else if (!isPhoneValid(phone)) {
            signupFormState.setValue(new SignupFormState(null,R.string.invalid_phone, null,null,null));
        }
        else if (isUserNameValid(username)) {
            signupFormState.setValue(new SignupFormState(null,null,R.string.invalid_username, null,null));
        } else if (!isPasswordValid(password)) {
            signupFormState.setValue(new SignupFormState(null,null,null, R.string.invalid_password,null));
        }else if (!isConfirmPasswordValid(cPassword)) {
            signupFormState.setValue(new SignupFormState(null,null,null,null, R.string.invalid_password));
        }else if (!isConfirmPasswordValid2(password,cPassword)) {
            signupFormState.setValue(new SignupFormState(null,null,null,null, R.string.password_miss_match));
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
        if (username == null) {
            return true;
        }
        if (username.contains("@")) {
            return !Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }

    private boolean isConfirmPasswordValid2(String password, String confirmPassword) {
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
                    signupResult.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CustomerJson> call,@NonNull Throwable t) {
                CustomerJson customerJson =new CustomerJson();
                customerJson.setStatus(false);
                customerJson.setMessage(t.getMessage());
                signupResult.postValue(customerJson);
            }
        });
    }
}