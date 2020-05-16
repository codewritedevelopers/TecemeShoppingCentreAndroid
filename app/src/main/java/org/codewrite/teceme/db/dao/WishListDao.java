package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.WishListEntity;

import java.util.List;

@Dao
public interface WishListDao {
    @Query("SELECT * FROM wish_list_table WHERE wishlist_access=1 AND wishlist_customer_id =:owner")
    LiveData<List<WishListEntity>> getWallet(String owner);

    @Query("SELECT * FROM wish_list_table WHERE wishlist_access=1 AND wishlist_id =:id AND wishlist_customer_id =:owner")
    LiveData<WishListEntity> getWallet(Integer id, String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WishListEntity entity);

    @Update
    void update(WishListEntity entity);

    @Delete
    void delete(WishListEntity entity);

    @Query("DELETE FROM wish_list_table WHERE wishlist_customer_id =:owner")
    void deleteAll(String owner);
}
