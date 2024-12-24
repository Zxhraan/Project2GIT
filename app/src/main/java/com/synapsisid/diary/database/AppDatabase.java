package com.synapsisid.diary.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {
        UserTable.class,
        DiaryTable.class
},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DatabaseDao databaseDao();

    private static  AppDatabase instance = null;

    public static  AppDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context,AppDatabase.class,"diary_db")
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

}
