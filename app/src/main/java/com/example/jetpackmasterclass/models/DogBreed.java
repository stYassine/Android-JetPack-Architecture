package com.example.jetpackmasterclass.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class DogBreed {

    @ColumnInfo(name = "breed_id")
    @SerializedName("id")
    public String breedId;

    @ColumnInfo(name = "breed_name")
    @SerializedName("name")
    public String dogBreed;

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    public String lifeSpan;

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    public String breedGroup;

    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    public String breedFor;

    @ColumnInfo(name = "temperament")
    @SerializedName("temperament")
    public String temperament;

    @ColumnInfo(name = "bred_url")
    @SerializedName("url")
    public String imageUrl;

    @PrimaryKey(autoGenerate = true)
    public int uuid;

    public DogBreed() {
    }

    public DogBreed(String breedId, String dogBreed, String lifeSpan, String breedGroup, String breedFor, String temperament, String imageUrl) {
        this.breedId = breedId;
        this.dogBreed = dogBreed;
        this.lifeSpan = lifeSpan;
        this.breedGroup = breedGroup;
        this.breedFor = breedFor;
        this.temperament = temperament;
        this.imageUrl = imageUrl;
    }





}
