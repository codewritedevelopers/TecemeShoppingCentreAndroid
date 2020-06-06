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
    LiveData<List<ProductEntity>> getProductsByCategoryId(Integer categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductEntity... entities);

    @Update
    void update(ProductEntity entity);

    @Delete
    void delete(ProductEntity entity);

    @Query("DELETE FROM product_table WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM product_table LIMIT :limit OFFSET :offset")
    LiveData<List<ProductEntity>> getAllProducts(int limit, int offset);

    @Query("SELECT * FROM product_table WHERE product_name LIKE :query OR product_desc LIKE :query")
    LiveData<List<ProductEntity>> searchProducts(String query);

    @Query("SELECT * FROM product_table WHERE product_name LIKE :query OR product_desc LIKE :query LIMIT 1")
    LiveData<ProductEntity> searchProduct(String query);
}
