package com.example.jetpackmasterclass.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jetpackmasterclass.R;
import com.example.jetpackmasterclass.databinding.ItemDogBinding;
import com.example.jetpackmasterclass.models.DogBreed;
import com.example.jetpackmasterclass.utils.Util;
import com.example.jetpackmasterclass.views.DogClickListener;
import com.example.jetpackmasterclass.views.ListFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.ViewHolder> implements DogClickListener {

    private List<DogBreed> dogsList;

    public DogsListAdapter(List<DogBreed> dogsList) {
        this.dogsList = dogsList;
    }

    public void updateDogsList(List<DogBreed> dogsList){
        this.dogsList.clear();
        this.dogsList.addAll(dogsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogBinding view = DataBindingUtil.inflate(inflater, R.layout.item_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DogBreed dogBreed = dogsList.get(position);
        holder.itemDogBinding.setDog(dogBreed);
        holder.itemDogBinding.setListener(this);
        /*
        ImageView imageView = holder.itemView.findViewById(R.id.imageView);
        TextView name = holder.itemView.findViewById(R.id.name);
        TextView lifeSpan = holder.itemView.findViewById(R.id.lifeSpan);
        LinearLayout dogLayout = holder.itemView.findViewById(R.id.dogLayout);

        name.setText(dogBreed.dogBreed);
        lifeSpan.setText(dogBreed.lifeSpan);
        Util.loadImage(imageView, dogBreed.imageUrl, Util.getProgressDrawable(imageView.getContext()));

        dogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFragmentDirections.ActionDetails action = ListFragmentDirections.actionDetails(dogBreed.uuid);
                action.setUuid(dogBreed.uuid);
                Navigation.findNavController(dogLayout).navigate(action);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return this.dogsList.size();
    }

    @Override
    public void onDogClicked(View view) {
        String uuIdString = ((TextView)view.findViewById(R.id.dogId)).getText().toString();
        int uuid = Integer.valueOf(uuIdString);
        ListFragmentDirections.ActionDetails action = ListFragmentDirections.actionDetails(uuid);
        action.setUuid(uuid);

        Navigation.findNavController(view).navigate(action);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ItemDogBinding itemDogBinding;

        public ViewHolder(@NonNull ItemDogBinding itemDogBinding) {
            super(itemDogBinding.getRoot());
            this.itemDogBinding = itemDogBinding;
        }
    }


}
