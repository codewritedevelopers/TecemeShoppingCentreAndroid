package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.StoreProductEntity;

import java.util.List;

@Dao
public interface StoreProductDao {

    @Query("SELECT * FROM store_product_table WHERE product_id =:id limit 1")
    LiveData<StoreProductEntity> getStoreProduct(Integer id);

    @Query("SELECT * FROM store_product_table WHERE product_category_id =:categoryId")
    LiveData<List<StoreProductEntity>> getStoreProductsByCategoryId(Integer categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoreProductEntity... entities);

    @Update
    void update(StoreProductEntity entity);

    @Delete
    void delete(StoreProductEntity entity);

    @Query("DELETE FROM store_product_table WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM store_product_table LIMIT :limit OFFSET :offset")
    LiveData<List<StoreProductEntity>> getAllStoreProducts(int limit, int offset);

    @Query("SELECT * FROM store_product_table WHERE product_name LIKE :query OR product_desc LIKE :query")
    LiveData<List<StoreProductEntity>> searchStoreProducts(String query);

    @Query("SELECT * FROM store_product_table WHERE product_name LIKE :query OR product_desc LIKE :query LIMIT 1")
    LiveData<StoreProductEntity> searchStoreProduct(String query);
}
