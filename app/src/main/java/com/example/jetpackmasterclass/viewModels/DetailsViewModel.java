package com.example.jetpackmasterclass.viewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jetpackmasterclass.models.DogBreed;
import com.example.jetpackmasterclass.room.DogDatabase;

public class DetailsViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();
    private RetrieveDogTask task;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetch(int uuid){
        task = new RetrieveDogTask();
        task.execute(uuid);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(task != null){
            task.cancel(true);
            task = null;
        }
    }

    private class RetrieveDogTask extends AsyncTask<Integer, Void, DogBreed> {

        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid = integers[0];

            return DogDatabase.getInstance(getApplication()).dogDao().getDog(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogLiveData.setValue(dogBreed);
        }
    }

}
