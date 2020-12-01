package org.codewrite.teceme.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.codewrite.teceme.db.dao.AccessTokenDao;
import org.codewrite.teceme.db.dao.CartDao;
import org.codewrite.teceme.db.dao.CategoryDao;
import org.codewrite.teceme.db.dao.CustomerDao;
import org.codewrite.teceme.db.dao.CustomerOrderDao;
import org.codewrite.teceme.db.dao.ProductDao;
import org.codewrite.teceme.db.dao.StoreDao;
import org.codewrite.teceme.db.dao.StoreProductDao;
import org.codewrite.teceme.db.dao.WalletDao;
import org.codewrite.teceme.db.dao.WalletLogDao;
import org.codewrite.teceme.db.dao.WishListDao;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.CustomerOrderEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.model.room.StoreProductEntity;
import org.codewrite.teceme.model.room.WalletEntity;
import org.codewrite.teceme.model.room.WalletLogEntity;
import org.codewrite.teceme.model.room.WishListEntity;

@Database(entities = {CustomerEntity.class, CategoryEntity.class, CartEntity.class, CustomerOrderEntity.class,
        ProductEntity.class, StoreEntity.class, WalletEntity.class, WalletLogEntity.class,
        WishListEntity.class, AccessTokenEntity.class, StoreProductEntity.class},version = 2, exportSchema = false)
public abstract class TecemeDataBase extends RoomDatabase {
    private static TecemeDataBase instance;
    public static synchronized TecemeDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TecemeDataBase.class,"teceme_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract CustomerDao customerDao();
    public abstract CategoryDao categoryDao();
    public abstract AccessTokenDao accessTokenDao();
    public abstract CartDao cartDao();
    public abstract CustomerOrderDao orderDao();
    public abstract ProductDao productDao();
    public abstract StoreProductDao storeProductDao();
    public abstract WalletDao walletDao();
    public abstract StoreDao storeDao();
    public abstract WalletLogDao walletLogDao();
    public abstract WishListDao wishListDao();
}
