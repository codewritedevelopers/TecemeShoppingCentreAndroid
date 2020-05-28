package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.codewrite.teceme.model.rest.StoreJson;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.repository.StoreRepository;
import org.codewrite.teceme.repository.StoreRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreViewModel extends AndroidViewModel {
    private StoreRepository storeRepository;
    private final int loadSize = 20;
    private int page;
    private int totalCount;
    private boolean moreItem;

    public StoreViewModel(@NonNull Application application) {
        super(application);
        storeRepository = new StoreRepository(application);
    }

    public LiveData<PagedList<StoreEntity>> getStores() {
        page = 0;
        moreItem = true;
        PagedList.BoundaryCallback<StoreEntity> boundaryCallback;

        boundaryCallback = new PagedList.BoundaryCallback<StoreEntity>() {
            @Override
            public void onZeroItemsLoaded() {
                super.onZeroItemsLoaded();
                loadStores();
            }

            @Override
            public void onItemAtFrontLoaded(@NonNull StoreEntity itemAtFront) {
                super.onItemAtFrontLoaded(itemAtFront);
                Log.d("StoreViewModel", "onItemAtFrontLoaded: ");
            }

            @Override
            public void onItemAtEndLoaded(@NonNull StoreEntity itemAtEnd) {
                super.onItemAtEndLoaded(itemAtEnd);
                if (moreItem) {
                    loadStores();
                }
                Log.d("StoreViewModel", "onItemAtEndLoaded: ");
            }
        };

        return new LivePagedListBuilder<>(storeRepository.getStores(), loadSize)
                .setBoundaryCallback(boundaryCallback)
                .build();
    }

    private void loadStores() {
        Call<List<StoreJson>> storeList = storeRepository.getStoreList(null,loadSize, page);
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
                    storeRepository.insert(storeEntities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoreJson>> call, @NonNull Throwable t) {
                Log.d("StoreDataSource", "onFailure: " + t.getMessage());
            }
        });
    }
}
