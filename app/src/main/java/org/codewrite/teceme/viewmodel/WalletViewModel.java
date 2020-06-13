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
import org.codewrite.teceme.model.form.WalletFormState;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.rest.WalletJson;
import org.codewrite.teceme.model.rest.WalletLogJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.WalletLogEntity;
import org.codewrite.teceme.repository.CustomerRepository;
import org.codewrite.teceme.repository.WalletRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AccountViewModel
 */
public class WalletViewModel extends AndroidViewModel {

    private boolean passwordMasked;
    private MutableLiveData<WalletFormState> walletFormState = new MutableLiveData<>();

    private WalletRepository walletRepository;
    private MutableLiveData<WalletLogJson> walletLogResult =new MutableLiveData<>();
    private MutableLiveData<WalletJson> walletResult = new MutableLiveData<>();

    public WalletViewModel(@NonNull Application application) {
        super(application);
        passwordMasked = true;
        walletRepository = new WalletRepository(application);
    }

    public LiveData<WalletFormState> getWalletFormState() {
        return walletFormState;
    }

    public LiveData<WalletJson> getWalletResult() {
        return walletResult;
    }

    public void walletFormDataChanged( String pinCode, String confirmPinCode, String secretAnswer) {
        if (!isPinCodeValid(pinCode)) {
            walletFormState.setValue(new WalletFormState(null, R.string.invalid_pin_code, null,null));
        } else if (!isPinCodeValid(confirmPinCode)) {
            walletFormState.setValue(new WalletFormState(null, null, R.string.pin_code_missmatch,null));
        } else if (!isConfirmPinCodeValid(pinCode,confirmPinCode)) {
            walletFormState.setValue(new WalletFormState(null, null, R.string.invalid_pin_code,null));
        } else if (!isSecretAnswer(secretAnswer)) {
            walletFormState.setValue(new WalletFormState(null, null, null, R.string.invalid_secret_answer));
        } else {
            walletFormState.setValue(new WalletFormState(true));
        }
    }

    private boolean isConfirmPinCodeValid(String pinCode, String confirmPinCode) {
        return pinCode.equals(confirmPinCode);
    }

    private boolean isSecretAnswer(String secretAnswer) {
        return !secretAnswer.isEmpty();
    }

    private boolean isPinCodeValid(String pinCode) {
        return pinCode!=null&&pinCode.length()>3;
    }
    private boolean isPhoneValid(String phone) {
        return phone != null && Patterns.PHONE.matcher(phone).matches();
    }
    public void create(String owner, String secretQuestion, String secretAnswer, String pinCode,String accessToken) {
        Call<WalletJson> wallet = walletRepository.create(owner,secretQuestion, secretAnswer, pinCode,accessToken);
        wallet.enqueue(new Callback<WalletJson>() {
            @Override
            public void onResponse(@NonNull Call<WalletJson> call,
                                   @NonNull Response<WalletJson> response) {
                if (response.isSuccessful()) {
                    walletResult.postValue(response.body());
                } else {
                    WalletJson walletJson = new WalletJson();
                    walletJson.setStatus(false);
                    walletJson.setMessage(response.message());
                    walletResult.postValue(walletJson);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WalletJson> call, @NonNull Throwable t) {
                WalletJson walletJson = new WalletJson();
                walletJson.setStatus(false);
                walletJson.setMessage(t.getMessage());
                walletResult.postValue(walletJson);
            }
        });
    }

    public void update(String owner, String pinCode, String accessToken) {
        Call<WalletJson> wallet
                = walletRepository.update(owner, pinCode, accessToken);
        wallet.enqueue(new Callback<WalletJson>() {
            @Override
            public void onResponse(@NonNull Call<WalletJson> call,
                                   @NonNull Response<WalletJson> response) {
                if (response.isSuccessful()) {
                    walletResult.postValue(response.body());
                } else {
                    WalletJson walletJson = new WalletJson();
                    walletJson.setStatus(false);
                    walletJson.setMessage(response.message());
                    walletResult.postValue(walletJson);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WalletJson> call, @NonNull Throwable t) {
                WalletJson walletJson = new WalletJson();
                walletJson.setStatus(false);
                if(t.getMessage()!=null) {
                    walletJson.setMessage(t.getMessage());
                }else{
                    walletJson.setMessage("Process timeout! Please, Try again");
                }
                walletResult.postValue(walletJson);
            }
        });
    }

    public void submitTransaction(String customer_id, long amount, String transType, String transactionTo,
                                  String accessToken) {

        Call<WalletJson> wallet
                = walletRepository.addWalletLog(customer_id,amount, transType,transactionTo,accessToken);
        wallet.enqueue(new Callback<WalletJson>() {
            @Override
            public void onResponse(@NonNull Call<WalletJson> call,
                                   @NonNull Response<WalletJson> response) {
                if (response.isSuccessful()) {
                    walletResult.postValue(response.body());
                    String token = response.headers().get("Token");
                } else {
                    WalletJson walletJson = new WalletJson();
                    walletJson.setStatus(false);
                    walletJson.setMessage(response.message());
                    walletResult.postValue(walletJson);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WalletJson> call, @NonNull Throwable t) {
                WalletJson walletJson = new WalletJson();
                walletJson.setStatus(false);
                if(t.getMessage()!=null) {
                    walletJson.setMessage(t.getMessage());
                }else{
                    walletJson.setMessage("Process timeout! Please, Try again");
                }
                walletResult.postValue(walletJson);
            }
        });
    }

    public MutableLiveData<WalletLogJson> getWalletLogResult() {
        return walletLogResult;
    }
}
