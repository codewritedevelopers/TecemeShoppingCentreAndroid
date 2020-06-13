package org.codewrite.teceme.repository;

import android.app.Application;

import org.codewrite.teceme.api.RestApi;
import org.codewrite.teceme.api.Service;

import java.util.List;

import retrofit2.Call;

public class AdsRepository {
   private RestApi restApi;
    public AdsRepository(Application application) {
         restApi = Service.getRestApi(application,null);
    }

    public Call<List<String>> getAds(){
        return restApi.getAds();
    }
}
