package org.codewrite.teceme.repository;

import android.app.Application;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.model.rest.WalletJson;

import retrofit2.Call;

public class WalletRepository {
    private RestApi resetApi;

    public WalletRepository(Application application) {
        resetApi = Service.getResetApi(application);
    }

    public Call<WalletJson> create(String owner, String secretQuestion, String secretAnswer, String pinCode, String accessToken) {
        WalletJson walletJson = new WalletJson();
        walletJson.setWallet_owner(owner);
        walletJson.setWallet_pin_code(pinCode);
        walletJson.setWallet_secret_question(secretQuestion);
        walletJson.setWallet_secret_answer(secretAnswer);
        return resetApi.createWallet(walletJson, "Bearer " + accessToken);
    }

    public Call<WalletJson> update(String owner, String pinCode, String accessToken, String walletId) {
        WalletJson walletJson = new WalletJson();
        walletJson.setWallet_owner(owner);
        walletJson.setWallet_pin_code(pinCode);
        return resetApi.updateWallet(walletJson, "Bearer " + accessToken);
    }
}
