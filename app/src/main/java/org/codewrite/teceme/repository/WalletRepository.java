package org.codewrite.teceme.repository;

import android.app.Application;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.model.rest.WalletJson;
import org.codewrite.teceme.model.rest.WalletLogJson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class WalletRepository {
    private RestApi restApi;
    private Application application;

    public WalletRepository(Application application) {
        this.application=application;
    }

    private void setRestApi(String accessToken) {
        Map<String,String> headers = new HashMap<>();
        headers.put("ACCESS_TOKEN","Bearer "+accessToken);
        this.restApi = Service.getRestApi(application,headers);
    }

    public Call<WalletJson> create(String owner, String secretQuestion, String secretAnswer,
                                   String pinCode, String accessToken) {
        WalletJson walletJson = new WalletJson();
        walletJson.setWallet_owner(owner);
        walletJson.setWallet_pin_code(pinCode);
        walletJson.setWallet_secret_question(secretQuestion);
        walletJson.setWallet_secret_answer(secretAnswer);
        setRestApi(accessToken);
        return restApi.createWallet(walletJson);
    }

    public Call<WalletJson> update(String owner, String pinCode, String accessToken) {
        WalletJson walletJson = new WalletJson();
        walletJson.setWallet_owner(owner);
        walletJson.setWallet_pin_code(pinCode);
        setRestApi(accessToken);
        return restApi.updateWallet(walletJson);
    }

    public Call<WalletJson> addWalletLog(String customer_id, long amountText,
                                         String transType, String transactionTo, String accessToken) {
        WalletLogJson walletLogJson = new WalletLogJson();
        walletLogJson.setWallet_log_amount(amountText);
        walletLogJson.setWallet_log_owner(customer_id);
        if (!transactionTo.isEmpty()) {
            walletLogJson.setWallet_log_transaction_to(transactionTo);
        }
        walletLogJson.setWallet_log_transaction_type(transType);

        setRestApi(accessToken);
        return restApi.addWalletLog(walletLogJson);
    }
}
