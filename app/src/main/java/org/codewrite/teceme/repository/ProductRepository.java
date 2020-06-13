package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.ProductDao;
import org.codewrite.teceme.db.dao.StoreProductDao;
import org.codewrite.teceme.model.rest.ProductJson;
import org.codewrite.teceme.model.rest.StoreProductJson;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.StoreProductEntity;

import java.util.List;

import retrofit2.Call;

public class ProductRepository {

    private ProductDao productDao;
    private StoreProductDao storeProductDao;
    private RestApi restApi;

    public ProductRepository(Application application) {
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        productDao = tecemeDataBase.productDao();
        storeProductDao = tecemeDataBase.storeProductDao();
        restApi = Service.getRestApi(application,null);
    }

    public void insert(CompleteAllCallback completecallBack, ProductEntity... entities){
        new InsertProductAsyncTask(productDao,completecallBack).execute(entities);
    }

    public void insert(CompleteAllCallback completecallBack, StoreProductEntity... entities){
        new InsertStoreProductAsyncTask(storeProductDao,completecallBack).execute(entities);
    }

    public LiveData<List<ProductEntity>>
    getProductsByCategoryId(Integer category_id) {
        return productDao.getProductsByCategoryId(category_id);
    }

    public Call<List<ProductJson>> getProductList(Integer categoryId, int loadSize, Integer page) {
        return restApi.getProductList(categoryId,loadSize,page);
    }

    public LiveData<ProductEntity> getProduct(Integer product_id) {
        return productDao.getProduct(product_id);
    }

    public LiveData<List<ProductEntity>> getAllProducts(int loadSize, int offset) {
        return productDao.getAllProducts(loadSize,offset);
    }

    public LiveData<List<ProductEntity>> searchProducts(String query) {
        return productDao.searchProducts("%"+query+"%");
    }

    public Call<List<ProductJson>> searchOnlineProducts(String s) {
        return restApi.getSearchProducts(s);
    }

    public LiveData<ProductEntity> searchProduct(String query) {
        return productDao.searchProduct(query);
    }

    public Call<ProductJson> loadProduct(Integer product_id) {
        return restApi.getProduct(product_id);
    }

    public Call<List<StoreProductJson>> loadStoreProduct(Integer store_id) {
        return restApi.getStoreProducts(store_id);
    }

    private static class DeleteAllProductAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProductDao productDao;
        private DeleteAllCallback deleteAllCallback;

        DeleteAllProductAsyncTask(ProductDao productDao, DeleteAllCallback deleteAllCallback) {
            this.productDao = productDao;
            this.deleteAllCallback = deleteAllCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (this.productDao != null) {
                this.productDao.deleteAll();
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

    private static class InsertProductAsyncTask extends AsyncTask<ProductEntity, Void, Void> {
        private ProductDao productDao;
        private CompleteAllCallback completeAllCallback;
        InsertProductAsyncTask(ProductDao productDao, CompleteAllCallback completeAllCallback) {
            this.productDao = productDao;
            this.completeAllCallback = completeAllCallback;
        }

        @Override
        protected Void doInBackground(ProductEntity... entities) {
            if (this.productDao != null) {
                this.productDao.insert(entities);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (completeAllCallback!=null){
                completeAllCallback.finish();
            }
        }
    }


    private static class InsertStoreProductAsyncTask extends AsyncTask<StoreProductEntity, Void, Void> {
        private StoreProductDao storeProductDao;
        private CompleteAllCallback completeAllCallback;
        InsertStoreProductAsyncTask(StoreProductDao productDao, CompleteAllCallback completeAllCallback) {
            this.storeProductDao = productDao;
            this.completeAllCallback = completeAllCallback;
        }

        @Override
        protected Void doInBackground(StoreProductEntity... entities) {
            if (this.storeProductDao != null) {
                this.storeProductDao.insert(entities);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (completeAllCallback!=null){
                completeAllCallback.finish();
            }
        }
    }

    interface DeleteAllCallback {
        void finish();
    }

    public interface CompleteAllCallback {
        void finish();
    }
}
