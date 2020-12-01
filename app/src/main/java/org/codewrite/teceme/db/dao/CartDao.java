package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.CartEntity;

import java.util.List;

@Dao
public interface CartDao {
    @Query("SELECT * FROM cart_table WHERE cart_owner=:owner AND cart_access=1")
    LiveData<List<CartEntity>> getCarts(String owner);

    @Query("SELECT SUM(cart_quantity*product_price) as total FROM cart_table WHERE cart_owner=:owner AND cart_access=1")
    LiveData<Double> getCartsTotal(String owner);

    @Query("SELECT * FROM cart_table WHERE cart_product_id=:product_id AND cart_owner=:owner AND cart_access=1 limit 1")
    LiveData<CartEntity> getCart(Integer product_id,String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartEntity... cartEntity);

    @Update
    void update(CartEntity... cartEntity);

    @Query("DELETE FROM cart_table WHERE cart_product_id =:product_id AND cart_access=1")
    void delete(Integer product_id);

    @Query("DELETE FROM cart_table")
    void deleteAll();

    @Query("DELETE FROM cart_table WHERE cart_date_created<>:date")
    void deleteAllOldCart(String date);
}
