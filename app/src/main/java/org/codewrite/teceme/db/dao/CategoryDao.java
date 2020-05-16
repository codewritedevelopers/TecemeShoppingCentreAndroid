package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category_table WHERE category_access= 1")
    LiveData<List<CartEntity>> getCategory();

    @Query("SELECT * FROM category_table WHERE category_access= 1 AND category_id =:id limit 1")
    LiveData<CategoryEntity> getCategory(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryEntity entity);

    @Update
    void update(CategoryEntity entity);

    @Delete
    void delete(CategoryEntity entity);

    @Query("DELETE FROM category_table WHERE 1")
    void deleteAll();
}
