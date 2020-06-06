package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.CategoryDao;
import org.codewrite.teceme.db.dao.ProductDao;
import org.codewrite.teceme.model.rest.CategoryJson;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.ProductEntity;

import java.util.List;

import retrofit2.Call;

public class CategoryRepository {
    private RestApi restApi;
    private CategoryDao categoryDao;
    public CategoryRepository(Application application){
        restApi = Service.getResetApi(application);
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        categoryDao = tecemeDataBase.categoryDao();
    }

    public Call<List<CategoryJson>> getCategoryByParent(Integer parent_id) {
        return restApi.getCategoryList(parent_id,null);
    }

    public void insert(CategoryEntity... entities){
        new InsertAsyncTask(categoryDao).execute(entities);
    }
    public void deleteAll(){
        new DeleteAllAsyncTask(categoryDao,null).execute();
    }

    public LiveData<List<CategoryEntity>> getCategoryEntityByParent(Integer parent_id) {
       return categoryDao.getCategoryByParent(parent_id);
    }

    public Call<List<CategoryJson>> getCategoryList() {
        return restApi.getCategoryList(null,null);
    }

    public LiveData<List<CategoryEntity>> getCategoryEntityHome() {
        return  categoryDao.getCategoryForHome();
    }

    public LiveData<List<CategoryEntity>> searchCategories(String query) {
        return  categoryDao.searchCategories("%"+query+"%");
    }

    public LiveData<CategoryEntity> getCategory(Integer category_id) {
       return categoryDao.getCategory(category_id);
    }

    public LiveData<CategoryEntity> searchCategory(String query) {
        return  categoryDao.searchCategory("%"+query+"%");
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private CategoryDao categoryDao;
        private DeleteAllCallback deleteAllCallback;

        DeleteAllAsyncTask(CategoryDao categoryDao, DeleteAllCallback deleteAllCallback) {
            this.categoryDao = categoryDao;
            this.deleteAllCallback = deleteAllCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (this.categoryDao != null) {
                this.categoryDao.deleteAll();
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

    private static class InsertAsyncTask extends AsyncTask<CategoryEntity, Void, Void> {
        private CategoryDao categoryDao;

        InsertAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(CategoryEntity... entities) {
            if (this.categoryDao != null) {
                this.categoryDao.insert(entities);
            }
            return null;
        }
    }

    interface DeleteAllCallback {
        void finish();
    }
}
