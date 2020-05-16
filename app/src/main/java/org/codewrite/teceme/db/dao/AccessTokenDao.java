package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CartEntity;

import java.util.List;

@Dao
public interface AccessTokenDao {
    @Query("SELECT * FROM access_token_table")
    LiveData<List<CartEntity>> getAccessToken();

    @Query("SELECT * FROM access_token_table WHERE id =:id limit 1")
    LiveData<AccessTokenEntity> getAccessToken(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccessTokenEntity entity);

    @Update
    void update(AccessTokenEntity entity);

    @Query("DELETE FROM access_token_table WHERE 1")
    void deleteAll();
}
