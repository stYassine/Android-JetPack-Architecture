package com.example.jetpackmasterclass.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jetpackmasterclass.models.DogBreed;

import java.util.List;
@Dao
public interface DogDao {

    @Insert()
    List<Long> insertAll(DogBreed ...dogs);

    @Query("SELECT * FROM dogbreed")
    List<DogBreed> getAllDogs();

    @Query("SELECT * FROM dogbreed WHERE uuid = :dogId")
    DogBreed getDog(int dogId);

    @Query("DELETE FROM dogbreed")
    void deleteAll();

}
