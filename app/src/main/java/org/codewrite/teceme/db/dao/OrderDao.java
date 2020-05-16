package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.holder.Order;
import org.codewrite.teceme.model.room.CartEntity;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM order_table WHERE order_access= 1")
    LiveData<List<CartEntity>> getOrder();

    @Query("SELECT * FROM order_table WHERE order_access= 1 AND order_id =:id limit 1")
    LiveData<Order> getOrder(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Order entity);

    @Update
    void update(Order entity);

    @Delete
    void delete(Order entity);

    @Query("DELETE FROM order_table WHERE 1")
    void deleteAll();
}
