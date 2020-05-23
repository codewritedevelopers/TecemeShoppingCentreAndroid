package org.codewrite.teceme.repository;

import android.app.Application;

import org.codewrite.teceme.model.rest.CategoryJson;

import java.util.List;

import retrofit2.Call;

public class CategoryRepository {
    CategoryRepository(Application application){

    }

    public Call<List<CategoryJson>> getCategories(Integer parent_id, int loadSize) {
        return null;
    }
}
