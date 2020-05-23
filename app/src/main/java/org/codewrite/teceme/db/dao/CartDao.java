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
    @Query("SELECT * FROM cart_table WHERE cart_access= 1 and cart_owner=:owner")
    LiveData<List<CartEntity>> getCart(String owner);

    @Query("SELECT * FROM cart_table WHERE cart_access= 1 AND cart_owner =:owner AND cart_product_id=:productId limit 1")
    LiveData<CartEntity> getCartByProduct(Integer owner, Integer productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartEntity cartEntity);

    @Update
    void update(CartEntity cartEntity);

    @Delete
    void delete(CartEntity cartEntity);

    @Query("DELETE FROM cart_table WHERE cart_owner=:owner")
    void deleteAll(String owner);
}
