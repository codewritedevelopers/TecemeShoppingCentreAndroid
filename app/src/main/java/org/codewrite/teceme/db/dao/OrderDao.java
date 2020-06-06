package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.OrderEntity;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM order_table")
    LiveData<List<OrderEntity>> getOrders();

    @Query("SELECT * FROM order_table WHERE order_id =:id limit 1")
    LiveData<OrderEntity> getOrder(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderEntity entity);

    @Update
    void update(OrderEntity entity);

    @Delete
    void delete(OrderEntity entity);

    @Query("DELETE FROM order_table WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM order_table WHERE order_status=:status AND order_owner=:owner")
    LiveData<List<OrderEntity>> getOrders(int status, String owner);
}
