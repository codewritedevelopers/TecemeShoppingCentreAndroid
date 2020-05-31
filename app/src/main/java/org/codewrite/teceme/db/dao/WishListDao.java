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
    @Query("SELECT * FROM wish_list_table WHERE wishlist_customer_id =:owner")
    LiveData<List<WishListEntity>> getWishLists(String owner);

    @Query("SELECT * FROM wish_list_table WHERE  wishlist_product_id =:productId")
    LiveData<WishListEntity> getWishListByProduct(Integer productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WishListEntity... entities);

    @Update
    void update(WishListEntity entity);

    @Query("DELETE FROM wish_list_table WHERE wishlist_product_id=:productId")
    void delete(Integer productId);

    @Query("DELETE FROM wish_list_table")
    void deleteAll();

}
