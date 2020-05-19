package org.codewrite.teceme.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import org.codewrite.teceme.model.rest.CategoryJson;
import org.codewrite.teceme.repository.CategoryRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDataSource extends ItemKeyedDataSource<Integer, CategoryJson>  {

    private CategoryRepository categoryRepository;
    private String TAG = "CategoryDataSource";

    public CategoryDataSource(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<CategoryJson> callback) {

        categoryRepository.getCategories(params.requestedInitialKey, params.requestedLoadSize)
                .enqueue(new Callback<List<CategoryJson>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<CategoryJson>> call,
                                           @NonNull Response<List<CategoryJson>> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            callback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<CategoryJson>> call,
                                          @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params,
                          @NonNull final LoadCallback<CategoryJson> callback) {
        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);

        categoryRepository.getCategories(params.key, params.requestedLoadSize)
                .enqueue(new Callback<List<CategoryJson>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<CategoryJson>> call,
                                           @NonNull Response<List<CategoryJson>> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            callback.onResult(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<CategoryJson>> call,
                                          @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<CategoryJson> callback) {

    }

    @NonNull
    @Override
    public Integer getKey(@NonNull CategoryJson item) {
        return item.getCategory_id();
    }
}
