package com.example.jetpackmasterclass.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.jetpackmasterclass.models.DogBreed;

@Database(entities = {DogBreed.class}, version = 1)
public abstract class DogDatabase  extends RoomDatabase {

    private static DogDatabase instance;

    public static DogDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DogDatabase.class,
                    "dogDatabase")
                    .build();
        }
        return instance;
    }

    public abstract DogDao dogDao();

}
