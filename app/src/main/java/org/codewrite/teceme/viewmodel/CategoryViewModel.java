package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.codewrite.teceme.model.rest.CategoryJson;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.repository.CategoryRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends AndroidViewModel {
    private int selectedHomeCategory;
    private CategoryRepository categoryRepository;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        selectedHomeCategory = 0;
        categoryRepository = new CategoryRepository(application);
    }

    public int getSelectedHomeCategory() {
        return selectedHomeCategory;
    }

    public void setSelectedHomeCategory(int selectedHomeCategory) {
        this.selectedHomeCategory = selectedHomeCategory;
    }

    public LiveData<List<CategoryEntity>> getCategoryByParentResult(Integer parent_id) {
        return  categoryRepository.getCategoryEntityByParent(parent_id);
    }


    public LiveData<List<CategoryEntity>> getCategoryForHomeResult() {
        return  categoryRepository.getCategoryEntityHome();
    }

    public void getCategoryList() {
        Call<List<CategoryJson>> call = categoryRepository.getCategoryList();
        call.enqueue(new Callback<List<CategoryJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryJson>> call,
                                   @NonNull Response<List<CategoryJson>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    CategoryEntity [] entities = new  CategoryEntity[response.body().size()];
                    int i = 0;
                    for (CategoryJson categoryJson : response.body()) {
                        entities[i++] = categoryJson;
                    }
                    categoryRepository.insert(entities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryJson>> call, @NonNull Throwable t) {
                Log.d("CategoryViewModel", "onFailure: "+t.getMessage());
            }
        });
    }

    public LiveData<CategoryEntity> getCategory(Integer category_id) {
        return categoryRepository.getCategory(category_id);
    }
}
