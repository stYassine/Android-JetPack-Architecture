package com.example.jetpackmasterclass.utils;

import com.example.jetpackmasterclass.models.DogBreed;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface DogsApi {

    @GET("DevTides/dogsApi/master/dogs.json")
    Single<List<DogBreed>> getDogs();

}
