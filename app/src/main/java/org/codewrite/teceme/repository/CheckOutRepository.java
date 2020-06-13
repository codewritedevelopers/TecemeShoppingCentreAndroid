package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.CustomerOrderDao;
import org.codewrite.teceme.db.dao.WishListDao;
import org.codewrite.teceme.model.rest.CustomerOrderJson;
import org.codewrite.teceme.model.rest.Result;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerOrderEntity;
import org.codewrite.teceme.model.room.WishListEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class CheckOutRepository {
    private RestApi restApi;
    private CustomerOrderDao customerOrderDao;
    private Application application;
    public CheckOutRepository(Application application) {
        this.application = application;
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        customerOrderDao =tecemeDataBase.orderDao();
    }

    private void setRestApi(String accessToken) {
        Map<String,String> headers = new HashMap<>();
        headers.put("ACCESS_TOKEN","Bearer "+accessToken);
        this.restApi = Service.getRestApi(application,headers);
    }


    public LiveData<List<CustomerOrderEntity>> getPendingOrders(String owner) {
        return customerOrderDao.getOrders(0, owner);
    }

    public Call<Result> checkoutCustomer(Map<String, Object> order,String method, String token) {
       setRestApi(token);
        return restApi.checkoutCustomer(order, method);
    }

    public void insertOrders(ProcessCallback callback, CustomerOrderEntity... customerOrderEntities) {
        new InsertCustomerOrdersAsyncTask(customerOrderDao,callback).execute(customerOrderEntities);
    }

    private static class InsertCustomerOrdersAsyncTask extends AsyncTask<CustomerOrderEntity, Void, Void> {
        private CustomerOrderDao customerOrderDao;
        private ProcessCallback processCallback;

        InsertCustomerOrdersAsyncTask(CustomerOrderDao customerOrderDao, ProcessCallback callback) {
            this.customerOrderDao = customerOrderDao;
            this.processCallback = callback;
        }

        @Override
        protected Void doInBackground(CustomerOrderEntity... entities) {
            if (this.customerOrderDao != null) {
                this.customerOrderDao.insert(entities);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (processCallback!=null) {
                processCallback.finish();
            }
        }
    }
    public interface ProcessCallback {
        void finish();
    }
}
