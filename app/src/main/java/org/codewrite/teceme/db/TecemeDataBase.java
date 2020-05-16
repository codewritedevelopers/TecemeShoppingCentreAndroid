package org.codewrite.teceme.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import org.codewrite.teceme.model.room.CustomerEntity;

@Database(entities = {CustomerEntity.class},version = 1)
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
}
