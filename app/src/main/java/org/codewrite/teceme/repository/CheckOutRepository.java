package org.codewrite.teceme.repository;

import android.app.Application;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;

public class CheckOutRepository {
    private RestApi restApi;
    public CheckOutRepository(Application application) {
        restApi = Service.getResetApi(application);
    }


}
