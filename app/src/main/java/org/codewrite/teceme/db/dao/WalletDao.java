package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.WalletEntity;
import org.codewrite.teceme.model.room.WalletLogEntity;

import java.util.List;

@Dao
public interface WalletDao {
    @Query("SELECT * FROM wallet_table WHERE wallet_access=1")
    LiveData<List<WalletLogEntity>> getWallet();

    @Query("SELECT * FROM wallet_table WHERE wallet_access=1 AND wallet_owner =:owner limit 1")
    LiveData<WalletLogEntity> getWallet(String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WalletEntity entity);

    @Update
    void update(WalletEntity entity);

    @Delete
    void delete(WalletEntity entity);

    @Query("DELETE FROM wallet_table WHERE 1")
    void deleteAll(String owner);
}