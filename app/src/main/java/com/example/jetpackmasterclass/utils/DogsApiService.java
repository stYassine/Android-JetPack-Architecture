package com.example.jetpackmasterclass.utils;

import com.example.jetpackmasterclass.models.DogBreed;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DogsApiService {

    private static final String BASE_URL = "https://raw.githubusercontent.com";

    private DogsApi dogsApi;

    public DogsApiService(){
        dogsApi = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DogsApi.class);
    }

    public Single<List<DogBreed>> getDogs(){
        return dogsApi.getDogs();
    }

}
