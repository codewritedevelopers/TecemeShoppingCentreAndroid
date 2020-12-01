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

public class CartRepository {
    private CartDao cartDao;
    private RestApi restApi;

    public CartRepository(Application application) {
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        cartDao = tecemeDataBase.cartDao();
        restApi = Service.getRestApi(application,null);
    }

    public void insert(CartEntity cartEntities){
        new InsertCartAsyncTask(cartDao).execute(cartEntities);
    }

    public LiveData<List<CartEntity>> getCarts(String owner) {
        return cartDao.getCarts(owner);
    }

    public LiveData<Double> getCartsTotal(String owner) {
        return cartDao.getCartsTotal(owner);
    }

    public LiveData<CartEntity> getCart(Integer product_id,String owner) {
        return cartDao.getCart(product_id,owner);
    }

    public void deleteCart(Integer product_id) {
       new DeleteCartAsyncTask(cartDao).execute(product_id);
    }

    public void update(CartEntity... cartEntity) {
        new UpdateCartAsyncTask(cartDao).execute(cartEntity);
    }

    public void deleteOldCarts(String today) {
        new DeleteAllOldCartAsyncTask(cartDao).execute(today);
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

    private static class DeleteAllOldCartAsyncTask extends AsyncTask<String, Void, Void> {
        private CartDao cartDao;

        DeleteAllOldCartAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(String... dates) {
            if (this.cartDao != null) {
                this.cartDao.deleteAllOldCart(dates[0]);
            }
            return null;
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

    private static class UpdateCartAsyncTask extends AsyncTask<CartEntity, Void, Void> {
        private CartDao cartDao;

        UpdateCartAsyncTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(CartEntity... entities) {
            if (this.cartDao != null) {
                this.cartDao.update(entities);
            }
            return null;
        }
    }

    interface DeleteAllCallback {
        void finish();
    }
}
