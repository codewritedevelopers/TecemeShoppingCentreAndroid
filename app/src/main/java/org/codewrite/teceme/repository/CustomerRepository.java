package org.codewrite.teceme.repository;

import android.app.Application;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.model.holder.Customer;
import org.codewrite.teceme.model.rest.CustomerJson;

import retrofit2.Call;
import retrofit2.Retrofit;

public class CustomerRepository {

    private RestApi resetApi;
    public CustomerRepository(Application application) {
         resetApi = Service.getResetApi(application);
    }

    public  Call<CustomerJson> login(String username, String password) {
        return resetApi.login(username, password);
    }
}
