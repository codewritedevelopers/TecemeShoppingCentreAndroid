package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.codewrite.teceme.model.rest.StoreJson;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.repository.StoreRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreViewModel extends AndroidViewModel {
    private StoreRepository storeRepository;
    private final int loadSize = 20;
    private int page;
    private boolean isLoading;
    private boolean moreItem;

    public StoreViewModel(@NonNull Application application) {
        super(application);
        storeRepository = new StoreRepository(application);
    }

    public LiveData<PagedList<StoreEntity>> getStores(final Integer category_id) {
        page = 0;
        moreItem = true;
        isLoading = false;
        PagedList.BoundaryCallback<StoreEntity> boundaryCallback;

        boundaryCallback = new PagedList.BoundaryCallback<StoreEntity>() {
            @Override
            public void onZeroItemsLoaded() {

                if (moreItem && !isLoading) {
                    loadStores(category_id);
                }
            }

            @Override
            public void onItemAtFrontLoaded(@NonNull StoreEntity itemAtFront) {

                Log.d("StoreViewModel", "onItemAtFrontLoaded: ");
            }

            @Override
            public void onItemAtEndLoaded(@NonNull StoreEntity itemAtEnd) {

                if (moreItem && !isLoading) {
                    loadStores(category_id);
                }
                Log.d("StoreViewModel", "onItemAtEndLoaded: ");
            }
        };

        return new LivePagedListBuilder<>(storeRepository.getStores(), loadSize)
                .setBoundaryCallback(boundaryCallback)
                .build();
    }

    private void loadStores(@Nullable Integer category_id) {
        isLoading = true;
        Call<List<StoreJson>> storeList = storeRepository.getStoreList(category_id,loadSize, page);
        storeList.enqueue(new Callback<List<StoreJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoreJson>> call,
                                   @NonNull Response<List<StoreJson>> response) {

                if (response.isSuccessful()) {
                    if (response.headers().get("MoreItem") != null) {
                        moreItem = true;
                        page++;
                    }else{
                        moreItem = false;
                    }
                    assert response.body() != null;
                    StoreEntity[] storeEntities = new StoreEntity[response.body().size()];
                    int i = 0;
                    for (StoreJson storeJson : response.body()) {
                        storeEntities[i++] = storeJson;
                    }
                    storeRepository.insert(new StoreRepository.CompletedCallback() {
                        @Override
                        public void finish() {
                            isLoading = false;
                        }
                    }, storeEntities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoreJson>> call, @NonNull Throwable t) {
                Log.d("StoreDataSource", "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<StoreEntity> getStore(int store_id) {
        return storeRepository.getStore(store_id);
    }
}
