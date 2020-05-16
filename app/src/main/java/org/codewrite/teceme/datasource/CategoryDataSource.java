package org.codewrite.teceme.datasource;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import org.codewrite.teceme.model.room.CategoryEntity;

public class CategoryDataSource extends ItemKeyedDataSource<Integer, CategoryEntity> {
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<CategoryEntity> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<CategoryEntity> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<CategoryEntity> callback) {

    }

    @NonNull
    @Override
    public Integer getKey(@NonNull CategoryEntity item) {
        return item.getId();
    }
}
