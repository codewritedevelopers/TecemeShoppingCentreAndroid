package org.codewrite.teceme.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.codewrite.teceme.repository.CheckOutRepository;

public class CheckOutViewModel extends AndroidViewModel {

    private CheckOutRepository checkOutRepository;
    public CheckOutViewModel(@NonNull Application application) {
        super(application);
        checkOutRepository = new CheckOutRepository(application);
    }
}
