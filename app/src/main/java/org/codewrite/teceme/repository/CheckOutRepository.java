package org.codewrite.teceme.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;
import org.codewrite.teceme.db.TecemeDataBase;
import org.codewrite.teceme.db.dao.OrderDao;
import org.codewrite.teceme.model.room.OrderEntity;

import java.util.List;

public class CheckOutRepository {
    private RestApi restApi;
    private OrderDao orderDao;
    public CheckOutRepository(Application application) {
        restApi = Service.getResetApi(application);
        TecemeDataBase tecemeDataBase = TecemeDataBase.getInstance(application);
        orderDao=tecemeDataBase.orderDao();
    }


    public LiveData<List<OrderEntity>> getPendingOrders(String owner) {
        return orderDao.getOrders(0, owner);
    }
}
