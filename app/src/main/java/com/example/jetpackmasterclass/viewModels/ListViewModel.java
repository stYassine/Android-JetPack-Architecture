package com.example.jetpackmasterclass.viewModels;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.jetpackmasterclass.models.DogBreed;
import com.example.jetpackmasterclass.room.DogDao;
import com.example.jetpackmasterclass.room.DogDatabase;
import com.example.jetpackmasterclass.utils.DogsApiService;
import com.example.jetpackmasterclass.utils.NotificationHelper;
import com.example.jetpackmasterclass.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    private DogsApiService dogsApiService = new DogsApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    private AsyncTask<List<DogBreed>, Void, List<DogBreed>> insertDogsTask;
    private AsyncTask<Void, Void, List<DogBreed>> retrieveDogsTask;

    private SharedPreferencesHelper preferencesHelper = SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L;


    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        checkCacheDuration();
        long updateTime = preferencesHelper.getUpdateTime();
        long currentTime = System.nanoTime();
        if(updateTime != 0 && currentTime - updateTime == refreshTime){
            fetchFromDatabase();
        }else{
            fetchFromRemote();
        }
    }

    public void refreshBypassCache(){
        fetchFromRemote();
    }

    public void checkCacheDuration(){
        String cachePreference = preferencesHelper.getCacheDuration();

        if(!cachePreference.equals("")){
            try {
                int cachePreferenceInt = Integer.parseInt(cachePreference);
                refreshTime = cachePreferenceInt * 1000 * 1000 * 1000L;
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
    }

    public void fetchFromDatabase(){
        loading.setValue(true);
        retrieveDogsTask = new RetrieveDogsTask();
        retrieveDogsTask.execute();
    }

    public void fetchFromRemote(){
        loading.setValue(true);
        disposable.add(
                dogsApiService.getDogs()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<DogBreed>>(){
                        @Override
                        public void onSuccess(List<DogBreed> dogBreeds) {
                            insertDogsTask = new InsertDogsTask();
                            insertDogsTask.execute(dogBreeds);
                            Toast.makeText(getApplication(), "DOGS FROM END-POINT", Toast.LENGTH_LONG).show();
                            NotificationHelper.getInstance(getApplication()).createNotification();
                        }

                        @Override
                        public void onError(Throwable e) {
                            loading.setValue(false);
                            dogLoadError.setValue(true);
                            e.printStackTrace();
                        }
                    })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if(insertDogsTask != null){
            insertDogsTask.cancel(true);
            insertDogsTask = null;
        }
        if(retrieveDogsTask != null){
            retrieveDogsTask.cancel(true);
            retrieveDogsTask = null;
        }
    }

    private void dogsRetrieved(List<DogBreed> list){
        dogs.setValue(list);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }

    private class InsertDogsTask extends AsyncTask<List<DogBreed>, Void, List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list = lists[0];

            DogDao dogDao = DogDatabase.getInstance(getApplication()).dogDao();
            dogDao.deleteAll();


            ArrayList<DogBreed> newList = new ArrayList<>(list);
            List<Long> result = dogDao.insertAll(newList.toArray(new DogBreed[0]));

            int i = 0;
            while(i < list.size()){
                list.get(i).uuid = result.get(i).intValue();
                i++;
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            preferencesHelper.saveUpdateTime(System.nanoTime());
        }
    }

    private class RetrieveDogsTask extends AsyncTask<Void, Void, List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(), "DOGS FROM DATABASE", Toast.LENGTH_LONG).show();
        }
    }

}
