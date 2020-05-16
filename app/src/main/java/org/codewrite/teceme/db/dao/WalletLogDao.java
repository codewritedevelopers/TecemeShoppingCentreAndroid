package org.codewrite.teceme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.codewrite.teceme.model.room.WalletLogEntity;

import java.util.List;

@Dao
public interface WalletLogDao {
    @Query("SELECT * FROM wallet_log_table WHERE wallet_log_access=1 AND wallet_log_owner =:owner")
    LiveData<List<WalletLogEntity>> getWalletLog(String owner);

    @Query("SELECT * FROM wallet_log_table WHERE wallet_log_access=1 AND wallet_log_id =:id AND wallet_log_owner =:owner limit 1")
    LiveData<WalletLogEntity> getWalletLog(Integer id, String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WalletLogEntity entity);

    @Update
    void update(WalletLogEntity entity);

    @Delete
    void delete(WalletLogEntity entity);

    @Query("DELETE FROM wallet_log_table WHERE wallet_log_owner =:owner")
    void deleteAll(String owner);
}
