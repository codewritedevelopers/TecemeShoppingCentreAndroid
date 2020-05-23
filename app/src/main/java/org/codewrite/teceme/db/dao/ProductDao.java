package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.ProductEntity;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product_table WHERE product_access= 1")
    LiveData<List<ProductEntity>> getProduct();

    @Query("SELECT * FROM product_table WHERE product_access= 1 AND product_id =:id limit 1")
    LiveData<ProductEntity> getProduct(Integer id);

    @Query("SELECT * FROM product_table WHERE product_access= 1 AND product_id =:id AND product_category_id =:categoryId limit 1")
    LiveData<ProductEntity> getProductByCategoryId(Integer id, Integer categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductEntity entity);

    @Update
    void update(ProductEntity entity);

    @Delete
    void delete(ProductEntity entity);

    @Query("DELETE FROM product_table WHERE 1")
    void deleteAll();
}
