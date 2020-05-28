package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.AccessTokenDao;
import org.codewrite.teceme.db.dao.CustomerDao;
import org.codewrite.teceme.db.dao.WishListDao;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.WishListEntity;

import java.util.List;

import retrofit2.Call;

public class CustomerRepository {

    private RestApi resetApi;
    private CustomerDao customerDao;
    private WishListDao wishListDao;
    private AccessTokenDao accessTokenDao;
    public CustomerRepository(Application application) {
         resetApi = Service.getResetApi(application);
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
         customerDao = tecemeDataBase.customerDao();
         accessTokenDao = tecemeDataBase.accessTokenDao();
         wishListDao = tecemeDataBase.wishListDao();
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

    public void replaceAccessToken(final AccessTokenEntity accessTokenEntity){
        new DeleteAllAccessTokenAsyncTask(accessTokenDao, new DeleteAllCallback() {
            @Override
            public void finish() {
                new InsertAccessTokenAsyncTask(accessTokenDao).execute(accessTokenEntity);
            }
        }).execute();
    }

    public void replaceLoggedInCustomer(final CustomerEntity customerEntity) {
       new DeleteAllCustomerAsyncTask(customerDao, new DeleteAllCallback() {
           @Override
           public void finish() {
               new InsertCustomerAsyncTask(customerDao).execute(customerEntity);
           }
       }).execute();
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

        CustomerJson customerJson = new CustomerJson();
        customerJson.setCustomer_first_name(firstName);
        customerJson.setCustomer_middle_name(middleName);
        customerJson.setCustomer_last_name(lastName);
        customerJson.setCustomer_username(username);
        customerJson.setCustomer_phone(phone);
        customerJson.setCustomer_password(password);
        return resetApi.signup(customerJson);
    }

    public void logoutCustomer() {
        new DeleteAllCustomerAsyncTask(customerDao,null).execute();
    }

    public Call<CustomerJson> updateCustomer(String name, String phone, String username,
                               String password, String currentPassword, String id, String accessToken) {
        String firstName, middleName=null, lastName=null;
        String [] parts = name.trim().split(" ");
        firstName = parts[0];
        if (parts.length > 2) {
            middleName = parts[1];
            lastName = parts[2];
        }else if(parts.length > 1){
            lastName = parts[1];
        }

        CustomerJson customerJson = new CustomerJson();
        customerJson.setCustomer_first_name(firstName);
        customerJson.setCustomer_middle_name(middleName);
        customerJson.setCustomer_last_name(lastName);
        customerJson.setCustomer_username(username);
        customerJson.setCustomer_phone(phone);
        customerJson.setCustomer_password(password);
        return resetApi.updateCustomer(customerJson,id,currentPassword,"Bearer "+accessToken);
    }

    public Call<CustomerJson> delete(String id, String accessToken) {

        CustomerJson customerJson = new CustomerJson();
       customerJson.setCustomer_access(false);
        return resetApi.updateCustomer(customerJson,id,accessToken);
    }

    public void deleteAllAccessToken() {
        new DeleteAllAccessTokenAsyncTask(accessTokenDao,null).execute();
    }

    public LiveData<WishListEntity> getWishList(String owner, int id) {
        return wishListDao.getWishListByProduct(owner,id);
    }

    public void addToWishList(WishListEntity wishListEntity) {

    }

    public Call<List<String>> getAdds() {
       return resetApi.getAds();
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
        private DeleteAllCallback deleteAllCallback;
        DeleteAllAccessTokenAsyncTask(AccessTokenDao accessTokenDao, DeleteAllCallback deleteAllCallback) {
            this.accessTokenDao = accessTokenDao;
            this.deleteAllCallback = deleteAllCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(this.accessTokenDao != null){
                this.accessTokenDao.deleteAll();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (deleteAllCallback !=null){
                deleteAllCallback.finish();
            }
        }
    }

    private static class DeleteAllCustomerAsyncTask extends AsyncTask<Void, Void, Void>{
        CustomerDao customerDao;
        DeleteAllCallback deleteAllCallback;
         DeleteAllCustomerAsyncTask(CustomerDao customerDao, DeleteAllCallback deleteAllCallback) {
            this.customerDao = customerDao;
            this.deleteAllCallback = deleteAllCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(this.customerDao != null){
                this.customerDao.deleteAll();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (deleteAllCallback !=null) {
                deleteAllCallback.finish();
            }
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

    interface DeleteAllCallback{
        void finish();
    }
}
