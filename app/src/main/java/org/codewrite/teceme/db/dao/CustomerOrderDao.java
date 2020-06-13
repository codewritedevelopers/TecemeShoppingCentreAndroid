package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.CustomerOrderEntity;

import java.util.List;

@Dao
public interface CustomerOrderDao {
    @Query("SELECT * FROM customer_order_table")
    LiveData<List<CustomerOrderEntity>> getCustomerOrders();

    @Query("SELECT * FROM customer_order_table WHERE customer_order_id =:id limit 1")
    LiveData<CustomerOrderEntity> getCustomerOrder(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CustomerOrderEntity... entity);

    @Update
    void update(CustomerOrderEntity... entity);

    @Delete
    void delete(CustomerOrderEntity entity);

    @Query("DELETE FROM customer_order_table WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM customer_order_table WHERE customer_order_status=:status " +
            "AND customer_order_customer_id=:owner")
    LiveData<List<CustomerOrderEntity>> getOrders(int status, String owner);
}
