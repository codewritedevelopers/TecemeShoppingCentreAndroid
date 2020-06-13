package org.codewrite.teceme.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.codewrite.teceme.repository.AdsRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsViewModel extends AndroidViewModel {
    private AdsRepository adsRepository;
    private MutableLiveData<List<String>> adsResult = new MutableLiveData<>();
    public AdsViewModel(@NonNull Application application) {
        super(application);
        this.adsRepository= new AdsRepository(application);
    }

    public void getAds(){
        adsRepository.getAds().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call,
                                   @NonNull Response<List<String>> response) {
                if (response.isSuccessful()){
                    adsResult.postValue(response.body());
                }else{
                    adsResult.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                    adsResult.postValue(null);
            }
        });
    }
    public LiveData<List<String>> getAdsResult() {
        return adsResult;
    }
}
