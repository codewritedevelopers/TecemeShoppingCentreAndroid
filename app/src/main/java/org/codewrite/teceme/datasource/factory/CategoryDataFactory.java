package org.codewrite.teceme.datasource.factory;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import org.codewrite.teceme.datasource.CategoryDataSource;
import org.codewrite.teceme.repository.CategoryRepository;

public class CategoryDataFactory extends DataSource.Factory {

    private MutableLiveData<CategoryDataSource> catDataSourceLiveData = new MutableLiveData<>();
    private CategoryRepository categoryRepository;
    public CategoryDataFactory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @NonNull
    @Override
    public DataSource create() {
        CategoryDataSource categoryDataSource = new CategoryDataSource(categoryRepository);
        catDataSourceLiveData.postValue(categoryDataSource);
        return categoryDataSource;
    }


    public LiveData<CategoryDataSource> getLiveData() {
        return catDataSourceLiveData;
    }
}
