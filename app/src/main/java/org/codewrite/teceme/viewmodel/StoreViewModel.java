package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.codewrite.teceme.model.rest.StoreJson;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.repository.StoreRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreViewModel extends AndroidViewModel {
    private StoreRepository storeRepository;

    public StoreViewModel(@NonNull Application application) {
        super(application);
        storeRepository = new StoreRepository(application);
    }

    public LiveData<List<StoreEntity>> getStoresByCategory(final Integer category_id) {
        Call<List<StoreJson>> storeList = storeRepository.getStoreListByCategory(category_id);
        storeList.enqueue(new Callback<List<StoreJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoreJson>> call,
                                   @NonNull Response<List<StoreJson>> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    StoreEntity[] storeEntities = new StoreEntity[response.body().size()];
                    int i = 0;
                    for (StoreJson storeJson : response.body()) {
                        storeEntities[i++] = storeJson;
                    }
                    storeRepository.insert(null, storeEntities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoreJson>> call, @NonNull Throwable t) {
                Log.d("StoreDataSource", "onFailure: " + t.getMessage());
            }
        });

        return storeRepository.getStoresByCategoryId(category_id);
    }


    public LiveData<List<StoreEntity>> getStores() {
        Call<List<StoreJson>> storeList = storeRepository.getStoreList();
        storeList.enqueue(new Callback<List<StoreJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoreJson>> call,
                                   @NonNull Response<List<StoreJson>> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    StoreEntity[] storeEntities = new StoreEntity[response.body().size()];
                    int i = 0;
                    for (StoreJson storeJson : response.body()) {
                        storeEntities[i++] = storeJson;
                    }
                    storeRepository.insert(null, storeEntities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoreJson>> call, @NonNull Throwable t) {
                Log.d("StoreDataSource", "onFailure: " + t.getMessage());
            }
        });

        return storeRepository.getStores();
    }

    public LiveData<StoreEntity> getStore(String store_id) {
        return storeRepository.getStore(store_id);
    }

    public LiveData<List<StoreEntity>> searchStores(String s) {
        return storeRepository.searchStores(s);
    }

    public LiveData<StoreEntity> searchStore(String title) {
        return storeRepository.searchStore(title);
    }

    public LiveData<List<StoreEntity>> getStoresByProduct(String customer_order_product_code) {
        final MutableLiveData<List<StoreEntity>> liveData = new MutableLiveData<>();
        Call<List<StoreEntity>> storeList = storeRepository.getStoresByProducts(customer_order_product_code);
        storeList.enqueue(new Callback<List<StoreEntity>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoreEntity>> call,
                                   @NonNull Response<List<StoreEntity>> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    liveData.setValue(response.body());
                }else{
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoreEntity>> call, @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }
}
