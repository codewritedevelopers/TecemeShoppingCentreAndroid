package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.ProductDao;
import org.codewrite.teceme.model.rest.ProductJson;
import org.codewrite.teceme.model.room.ProductEntity;

import java.util.List;

import retrofit2.Call;

public class ProductRepository {

    private ProductDao productDao;
    private RestApi restApi;

    public ProductRepository(Application application) {
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        productDao = tecemeDataBase.productDao();
        restApi = Service.getResetApi(application);
    }

    public void insert(CompleteAllCallback completecallBack, ProductEntity... productEntities){
        new InsertProductAsyncTask(productDao,completecallBack).execute(productEntities);
    }

    public DataSource.Factory<Integer, ProductEntity> getProductsByCategoryId(Integer category_id) {
        return productDao.getProductsByCategoryId(category_id);
    }

    public Call<List<ProductJson>> getProductList(Integer categoryId, int loadSize, Integer page) {
        return restApi.getProductList(categoryId,loadSize,page);
    }

    public LiveData<ProductEntity> getProduct(Integer product_id) {
        return productDao.getProduct(product_id);
    }

    public DataSource.Factory<Integer,ProductEntity> getAllProducts() {
        return productDao.getAllProducts();
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

    interface DeleteAllCallback {
        void finish();
    }

    public interface CompleteAllCallback {
        void finish();
    }
}
