package org.codewrite.teceme.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import org.codewrite.teceme.model.room.OrderEntity;
import org.codewrite.teceme.repository.CheckOutRepository;

import java.util.List;

public class CheckOutViewModel extends AndroidViewModel {

    private CheckOutRepository checkOutRepository;
    public CheckOutViewModel(@NonNull Application application) {
        super(application);
        checkOutRepository = new CheckOutRepository(application);
    }


    public LiveData<List<OrderEntity>> getPendingOrders(String owner) {
        return checkOutRepository.getPendingOrders(owner);
    }
}
