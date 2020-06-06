package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.ProductEntity;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category_table")
    LiveData<List<CategoryEntity>> getCategory();

    @Query("SELECT * FROM category_table WHERE category_id =:id limit 1")
    LiveData<CategoryEntity> getCategory(Integer id);

    @Query("SELECT * FROM category_table WHERE category_parent_id =:parent_id")
    LiveData<List<CategoryEntity>> getCategoryByParent(Integer parent_id);

    @Query("SELECT * FROM category_table WHERE category_level=0")
    LiveData<List<CategoryEntity>> getCategoryForHome();

    @Query("SELECT * FROM category_table WHERE category_name LIKE :query")
    LiveData<List<CategoryEntity>> searchCategories(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryEntity... entity);

    @Update
    void update(CategoryEntity entity);

    @Delete
    void delete(CategoryEntity entity);

    @Query("DELETE FROM category_table WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM category_table WHERE category_level>0 AND category_name LIKE :query LIMIT 1 ")
    LiveData<CategoryEntity> searchCategory(String query);
}
