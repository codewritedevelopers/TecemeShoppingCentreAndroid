package org.codewrite.teceme.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import org.codewrite.teceme.model.room.CategoryEntity;

public class CategoryViewModel extends AndroidViewModel {
    private int selectedHomeCategory;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        selectedHomeCategory = 0;
    }

    public int getSelectedHomeCategory() {
        return selectedHomeCategory;
    }

    public void setSelectedHomeCategory(int selectedHomeCategory) {
        this.selectedHomeCategory = selectedHomeCategory;
    }

    public LiveData<PagedList<CategoryEntity>> getCategories(Integer category_level, Integer parent_id) {

        return null;
    }
}
