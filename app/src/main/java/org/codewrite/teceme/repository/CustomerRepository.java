package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.AccessTokenDao;
import org.codewrite.teceme.db.dao.CustomerDao;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;

import retrofit2.Call;

public class CustomerRepository {

    private RestApi resetApi;
    private LiveData<CustomerEntity> loggedInCustomer;
    private CustomerDao customerDao;
    private AccessTokenDao accessTokenDao;
    public CustomerRepository(Application application) {
         resetApi = Service.getResetApi(application);
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
         customerDao = tecemeDataBase.customerDao();
         accessTokenDao = tecemeDataBase.accessTokenDao();
    }

    public  Call<CustomerJson> login(String username, String password) {
        return resetApi.login(username, password);
    }

    public LiveData<CustomerEntity> getLoggedInCustomer() {
        return customerDao.getLoggedInCustomer();
    }

    public LiveData<AccessTokenEntity> getAccessToken() {
        return accessTokenDao.getAccessToken();
    }

    public void replaceAccessToken(AccessTokenEntity accessTokenEntity){
        new DeleteAllAccessTokenAsyncTask(accessTokenDao).execute();
        new InsertAccessTokenAsyncTask(accessTokenDao).execute(accessTokenEntity);
    }

    public void updateAccessToken(AccessTokenEntity accessTokenEntity){
        new InsertAccessTokenAsyncTask(accessTokenDao).execute(accessTokenEntity);
    }

    public void replaceLoggedInCustomer(CustomerEntity customerEntity) {
        new DeleteAllCustomerAsyncTask(customerDao).execute();
        new InsertCustomerAsyncTask(customerDao).execute(customerEntity);
    }

    public Call<CustomerJson> signup(String name, String phone, String username, String password) {
        String firstName, middleName=null, lastName=null;
        String [] parts = name.trim().split(" ");
        firstName = parts[0];
        if (parts.length > 2) {
            middleName = parts[1];
            lastName = parts[2];
        }else if(parts.length > 1){
            lastName = parts[1];
        }
        return resetApi.signup(new CustomerJson(username,password,firstName,middleName,lastName, phone,null));
    }

    private static class InsertAccessTokenAsyncTask extends AsyncTask<AccessTokenEntity, Void, Void> {
        private AccessTokenDao accessTokenDao;

        InsertAccessTokenAsyncTask(AccessTokenDao accessTokenDao) {
            this.accessTokenDao = accessTokenDao;
        }

        @Override
        protected Void doInBackground(AccessTokenEntity... accessTokenEntities) {

            if(this.accessTokenDao != null){
                this.accessTokenDao.insert(accessTokenEntities[0]);
            }
            return null;
        }
    }

    private static class DeleteAllAccessTokenAsyncTask extends AsyncTask<Void, Void, Void> {
        private AccessTokenDao accessTokenDao;

        DeleteAllAccessTokenAsyncTask(AccessTokenDao accessTokenDao) {
            this.accessTokenDao = accessTokenDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(this.accessTokenDao != null){
                this.accessTokenDao.deleteAll();
            }
            return null;
        }
    }

    private static class DeleteAllCustomerAsyncTask extends AsyncTask<Void, Void, Void>{
        CustomerDao customerDao;
        public DeleteAllCustomerAsyncTask(CustomerDao customerDao) {
            this.customerDao = customerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(this.customerDao != null){
                this.customerDao.deleteAll();
            }
            return null;
        }
    }

    private static class InsertCustomerAsyncTask extends AsyncTask<CustomerEntity, Void, Void>{
        CustomerDao customerDao;
         InsertCustomerAsyncTask(CustomerDao customerDao) {
            this.customerDao = customerDao;
        }

        @Override
        protected Void doInBackground(CustomerEntity... customerEntities) {
            if(this.customerDao != null){
                this.customerDao.insert(customerEntities[0]);
            }
            return null;
        }
    }
}
