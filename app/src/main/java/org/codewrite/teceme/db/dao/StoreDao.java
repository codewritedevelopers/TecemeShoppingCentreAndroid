package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
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
    @Query("SELECT * FROM store_table WHERE store_access= 1")
    LiveData<List<CustomerEntity>> getStore();

    @Query("SELECT * FROM store_table WHERE store_access= 1 AND store_id =:id limit 1")
    LiveData<StoreEntity> getStore(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoreEntity entity);

    @Update
    void update(StoreEntity entity);

    @Delete
    void delete(StoreEntity entity);

    @Query("DELETE FROM store_table WHERE 1")
    void deleteAll();
}
