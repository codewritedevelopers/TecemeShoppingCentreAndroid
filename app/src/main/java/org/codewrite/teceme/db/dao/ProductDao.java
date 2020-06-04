package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;
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

    @Query("SELECT * FROM product_table WHERE product_id =:id limit 1")
    LiveData<ProductEntity> getProduct(Integer id);

    @Query("SELECT * FROM product_table WHERE product_category_id =:categoryId")
    DataSource.Factory<Integer, ProductEntity> getProductsByCategoryId(Integer categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductEntity... entities);

    @Update
    void update(ProductEntity entity);

    @Delete
    void delete(ProductEntity entity);

    @Query("DELETE FROM product_table WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM product_table")
    DataSource.Factory<Integer,ProductEntity> getAllProducts();
}
