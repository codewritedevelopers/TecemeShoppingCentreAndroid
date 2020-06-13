package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.StoreEntity;

import java.util.List;

@Dao
public interface StoreDao {
    @Query("SELECT * FROM store_table WHERE store_access=1")
    LiveData<List<StoreEntity>> getStores();

    @Query("SELECT * FROM store_table WHERE store_id =:id AND store_access=1 limit 1")
    LiveData<StoreEntity> getStore(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoreEntity... entity);

    @Update
    void update(StoreEntity entity);

    @Delete
    void delete(StoreEntity entity);

    @Query("DELETE FROM store_table WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM store_table WHERE store_category_id =:category_id AND store_access=1")
    LiveData<List<StoreEntity>> getStoresByCategoryId(Integer category_id);

    @Query("SELECT * FROM store_table WHERE store_access=1 AND store_name LIKE :query OR store_desc OR store_location LIKE :query LIKE :query")
    LiveData<List<StoreEntity>> searchStores(String query);

    @Query("SELECT * FROM store_table WHERE store_access=1 AND store_name LIKE :query OR store_desc LIKE :query OR store_location LIKE :query LIMIT 1")
    LiveData<StoreEntity> searchStore(String query);
}
