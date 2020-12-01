package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.StoreDao;
import org.codewrite.teceme.model.rest.StoreJson;
import org.codewrite.teceme.model.room.StoreEntity;

import java.util.List;

import retrofit2.Call;

public class StoreRepository {

    private StoreDao storeDao;
    private RestApi restApi;

    public StoreRepository(Application application) {
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        storeDao = tecemeDataBase.storeDao();
        restApi = Service.getRestApi(application,null);
    }

    public void insert(CompletedCallback completeCallBack, StoreEntity... storeEntities){
        new InsertStoreAsyncTask(storeDao,completeCallBack ).execute(storeEntities);
    }

    public LiveData<List<StoreEntity>> getStoresByCategoryId(Integer category_id) {
        return storeDao.getStoresByCategoryId(category_id);
    }

    public Call<List<StoreJson>> getStoreList() {
        return restApi.getStoreList();
    }

    public LiveData<List<StoreEntity>> getStores() {
        return storeDao.getStores();
    }

    public LiveData<StoreEntity> getStore(String store_id) {
        return storeDao.getStore(store_id);
    }

    public LiveData<List<StoreEntity>> searchStores(String s) {
        return storeDao.searchStores("%"+s+"%");
    }

    public LiveData<StoreEntity> searchStore(String s) {
        return storeDao.searchStore("%"+s+"%");
    }

    public Call<List<StoreJson>> getStoreListByCategory(Integer category_id) {
        return restApi.getStoreListByCategory(category_id);
    }

    public Call<List<StoreEntity>> getStoresByProducts(String customer_order_product_code) {
        return restApi.getStoreListByProduct(customer_order_product_code);
    }

    private static class DeleteAllStoreAsyncTask extends AsyncTask<Void, Void, Void> {
        private StoreDao storeDao;
        private DeleteAllCallback deleteAllCallback;

        DeleteAllStoreAsyncTask(StoreDao storeDao, DeleteAllCallback deleteAllCallback) {
            this.storeDao = storeDao;
            this.deleteAllCallback = deleteAllCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (this.storeDao != null) {
                this.storeDao.deleteAll();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (deleteAllCallback != null) {
                deleteAllCallback.finish();
            }
        }
    }

    private static class InsertStoreAsyncTask extends AsyncTask<StoreEntity, Void, Void> {
        private StoreDao storeDao;
        private CompletedCallback completedCallback;
        InsertStoreAsyncTask(StoreDao storeDao, CompletedCallback completedCallback) {
            this.storeDao = storeDao;
            this.completedCallback = completedCallback;
        }

        @Override
        protected Void doInBackground(StoreEntity... entities) {
            if (this.storeDao != null) {
                this.storeDao.insert(entities);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (completedCallback!=null){
                completedCallback.finish();
            }
        }
    }

    interface DeleteAllCallback {
        void finish();
    }

    public interface CompletedCallback {
        void finish();
    }
}
