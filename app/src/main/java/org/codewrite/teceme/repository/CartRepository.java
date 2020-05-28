package org.codewrite.teceme.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.CartDao;
import org.codewrite.teceme.model.room.CartEntity;

import java.util.List;

import retrofit2.Call;

public class CartRepository {
    private CartDao cartDao;
    private RestApi restApi;

    public CartRepository(Application application) {
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        cartDao = tecemeDataBase.cartDao();
        restApi = Service.getResetApi(application);
    }

    public void insert(CartEntity cartEntities){
        new InsertCartAsyncTask(cartDao).execute(cartEntities);
    }

    public LiveData<List<CartEntity>> getCarts(String owner) {
        return cartDao.getCarts(owner);
    }

    public LiveData<CartEntity> getCart(Integer product_id) {
        return cartDao.getCart(product_id);
    }

    public void deleteCart(Integer product_id) {
       new DeleteCartAsyncTask(cartDao).execute(product_id);
    }

    private static class DeleteCartAsyncTask extends AsyncTask<Integer, Void, Void> {
        private CartDao cartDao;

        DeleteCartAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            if (this.cartDao != null) {
                this.cartDao.delete(ids[0]);
            }
            return null;
        }
    }

    private static class DeleteAllCartAsyncTask extends AsyncTask<Void, Void, Void> {
        private CartDao cartDao;
        private CartRepository.DeleteAllCallback deleteAllCallback;

        DeleteAllCartAsyncTask(CartDao cartDao, CartRepository.DeleteAllCallback deleteAllCallback) {
            this.cartDao = cartDao;
            this.deleteAllCallback = deleteAllCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (this.cartDao != null) {
                this.cartDao.deleteAll();
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

    private static class InsertCartAsyncTask extends AsyncTask<CartEntity, Void, Void> {
        private CartDao cartDao;

        InsertCartAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(CartEntity... entities) {
            if (this.cartDao != null) {
                this.cartDao.insert(entities);
            }
            return null;
        }
    }

    interface DeleteAllCallback {
        void finish();
    }
}
