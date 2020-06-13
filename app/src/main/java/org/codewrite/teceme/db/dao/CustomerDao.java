package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.CustomerEntity;

import java.util.List;

@Dao
public interface CustomerDao {
    @Query("SELECT * FROM customer_table WHERE customer_access=1 limit 1")
    LiveData<CustomerEntity> getLoggedInCustomer();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CustomerEntity entity);

    @Update
    void update(CustomerEntity entity);

    @Delete
    void delete(CustomerEntity entity);

    @Query("DELETE FROM customer_table WHERE 1")
    void deleteAll();
}
