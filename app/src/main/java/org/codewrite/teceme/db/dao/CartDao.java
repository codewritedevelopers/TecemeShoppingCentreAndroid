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
    @Query("SELECT * FROM cart_table WHERE cart_access= 1")
    LiveData<List<CartEntity>> getCart();

    @Query("SELECT * FROM cart_table WHERE cart_access= 1 AND cart_id =:id limit 1")
    LiveData<CartEntity> getCart(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartEntity cartEntity);

    @Update
    void update(CartEntity cartEntity);

    @Delete
    void delete(CartEntity cartEntity);

    @Query("DELETE FROM cart_table WHERE 1")
    void deleteAll();
}
