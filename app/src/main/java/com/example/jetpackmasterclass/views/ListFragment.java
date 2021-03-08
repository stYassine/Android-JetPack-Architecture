package com.example.jetpackmasterclass.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jetpackmasterclass.R;
import com.example.jetpackmasterclass.adapters.DogsListAdapter;
import com.example.jetpackmasterclass.models.DogBreed;
import com.example.jetpackmasterclass.viewModels.ListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private ListViewModel listViewModel;
    private DogsListAdapter dogsListAdapter = new DogsListAdapter(new ArrayList<>());

    @BindView(R.id.listRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.dogsList)
    RecyclerView dogsList;

    @BindView(R.id.listError)
    TextView listError;

    @BindView(R.id.loadingView)
    ProgressBar loadingView;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel.refresh();

        dogsList.setLayoutManager(new LinearLayoutManager(getContext()));
        dogsList.setAdapter(dogsListAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dogsList.setVisibility(View.GONE);
                listError.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                listViewModel.refreshBypassCache();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        this.observeViewModel();
    }

    private void observeViewModel(){
        listViewModel.dogs.observe(this, new Observer<List<DogBreed>>() {
            @Override
            public void onChanged(List<DogBreed> dogs) {
                if(dogs != null && dogs instanceof List){
                    dogsList.setVisibility(View.VISIBLE);
                    dogsListAdapter.updateDogsList(dogs);
                }
            }
        });
        listViewModel.dogLoadError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if(isError != null && isError instanceof Boolean){
                    listError.setVisibility(isError ? View.VISIBLE : View.GONE);
                }
            }
        });
        listViewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading != null && isLoading instanceof Boolean){
                    loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                    if(isLoading){
                        dogsList.setVisibility(View.GONE);
                        listError.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionSettings:
                if(isAdded()){
                    Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSettings());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
