package com.example.jetpackmasterclass.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jetpackmasterclass.R;

public class Util {

    public static void loadImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable){
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(progressDrawable)
                .error(R.drawable.dog_icon);

        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .into(imageView);
    }

    public static CircularProgressDrawable getProgressDrawable(Context context){
        CircularProgressDrawable cpd = new CircularProgressDrawable(context);
        cpd.setStrokeWidth(10f);
        cpd.setCenterRadius(50f);
        cpd.start();

        return cpd;
    }

    @BindingAdapter("android:imageUrl")
    public static void loadImage(ImageView imageView, String url){
        loadImage(imageView, url, getProgressDrawable(imageView.getContext()));
    }

}
