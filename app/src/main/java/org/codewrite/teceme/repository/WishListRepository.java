package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.WishListDao;
import org.codewrite.teceme.model.rest.WishListJson;
import org.codewrite.teceme.model.room.WishListEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class WishListRepository {
    private WishListDao wishListDao;
    private RestApi restApi;
    private Application application;

    public WishListRepository(Application application) {
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        wishListDao = tecemeDataBase.wishListDao();
        this.application=application;
    }

    private void setRestApi(String accessToken) {
        Map<String,String> headers = new HashMap<>();
        headers.put("ACCESS_TOKEN","Bearer "+accessToken);
        this.restApi = Service.getRestApi(application,headers);
    }

    public void insert(WishListEntity... wishListEntities) {
        new InsertWishListAsyncTask(wishListDao).execute(wishListEntities);
    }

    public void deleteAll() {
        new DeleteAllWishListAsyncTask(wishListDao, null).execute();
    }

    public LiveData<List<WishListEntity>> getWishListsResult(String owner) {
        return wishListDao.getWishLists(owner);
    }

    public Call<List<WishListJson>> getWishLists(String accessToken) {
        setRestApi(accessToken);
        return restApi.getWishList();
    }
    public Call<WishListJson> addToWishList(Integer product_id, String owner, String accessToken) {
        WishListJson wishListJson = new WishListJson();
        wishListJson.setWishlist_customer_id(owner);
        wishListJson.setWishlist_product_id(product_id);

        setRestApi(accessToken);
        return restApi.addToWishList(wishListJson);
    }

    public Call<WishListJson> removeWishList(String id,String accessToken) {
        setRestApi(accessToken);
        return restApi.deleteWishList(id);
    }

    public void delete(String id) {
        new DeleteWishListAsyncTask(wishListDao).execute(id);
    }

    public LiveData<WishListEntity> getWishList(Integer product_id, String customer_id) {
        return wishListDao.getWishListByProduct(product_id,customer_id);
    }

    private static class DeleteAllWishListAsyncTask extends AsyncTask<Void, Void, Void> {
        private WishListDao wishListDao;
        private WishListRepository.DeleteAllCallback deleteAllCallback;

        DeleteAllWishListAsyncTask(WishListDao wishListDao, WishListRepository.DeleteAllCallback deleteAllCallback) {
            this.wishListDao = wishListDao;
            this.deleteAllCallback = deleteAllCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (this.wishListDao != null) {
                this.wishListDao.deleteAll();
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

    private static class DeleteWishListAsyncTask extends AsyncTask<String, Void, Void> {
        private WishListDao wishListDao;

        DeleteWishListAsyncTask(WishListDao wishListDao) {
            this.wishListDao = wishListDao;
        }

        @Override
        protected Void doInBackground(String... ids) {
            if (this.wishListDao != null) {
                this.wishListDao.delete(ids[0]);
            }
            return null;
        }
    }

    private static class InsertWishListAsyncTask extends AsyncTask<WishListEntity, Void, Void> {
        private WishListDao wishListDao;

        InsertWishListAsyncTask(WishListDao wishListDao) {
            this.wishListDao = wishListDao;
        }

        @Override
        protected Void doInBackground(WishListEntity... entities) {
            if (this.wishListDao != null) {
                this.wishListDao.insert(entities);
            }
            return null;
        }
    }

    interface DeleteAllCallback {
        void finish();
    }
}
