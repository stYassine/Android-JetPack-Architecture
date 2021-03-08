package com.example.jetpackmasterclass.views;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jetpackmasterclass.R;
import com.example.jetpackmasterclass.databinding.FragmentDetailsBinding;
import com.example.jetpackmasterclass.databinding.SendSmsDialogBinding;
import com.example.jetpackmasterclass.models.DogBreed;
import com.example.jetpackmasterclass.models.SmsInfo;
import com.example.jetpackmasterclass.utils.DogPalete;
import com.example.jetpackmasterclass.utils.Util;
import com.example.jetpackmasterclass.viewModels.DetailsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private DetailsViewModel detailsViewModel;
    private FragmentDetailsBinding binding;
    private int dogUuid;

    private DogBreed currentDog;

    private boolean sendSmsStarted = false;

    @BindView(R.id.dogImage)
    ImageView dogImage;

    @BindView(R.id.dogName)
    TextView dogName;

    @BindView(R.id.dogPurpose)
    TextView dogPurpose;

    @BindView(R.id.dogTemperament)
    TextView dogTemperament;

    @BindView(R.id.dogLifeSpan)
    TextView dogLifeSpan;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        this.binding = binding;
        setHasOptionsMenu(true);
        /// we Can Set Text like this
        /// binding.dogName.setText("Pitbull");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null){
            dogUuid = DetailsFragmentArgs.fromBundle(getArguments()).getUuid();
        }

        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        detailsViewModel.fetch(dogUuid);

        observeViewModel();
    }

    private void observeViewModel(){
        detailsViewModel.dogLiveData.observe(this, new Observer<DogBreed>() {
            @Override
            public void onChanged(DogBreed dogBreed) {
                if(dogBreed != null && dogBreed instanceof DogBreed && getContext() != null){
                    currentDog = dogBreed;
                    binding.setDog(dogBreed);
                    if(dogBreed.imageUrl != null){
                        setBackgroundColor(dogBreed.imageUrl);
                    }
                }
            }
        });
    }

    private void setBackgroundColor(String url){
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                   int intColor = palette.getLightMutedSwatch().getRgb();
                                   DogPalete dogPalete = new DogPalete(intColor);
                                   binding.setPalette(dogPalete);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send_sms: {
                if(!sendSmsStarted){
                    sendSmsStarted = true;
                    ((MainActivity) getActivity()).checkSmsPermission();
                }
                break;
            }
            case R.id.action_share: {
                share();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPermissionResult(Boolean permissionGranted){
        if(isAdded() && sendSmsStarted && permissionGranted){
            String smsText = currentDog.dogBreed +"Breed For : "+ currentDog.breedFor;
            SmsInfo smsInfo = new SmsInfo("",smsText,currentDog.imageUrl);

            SendSmsDialogBinding smsDialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),
                    R.layout.send_sms_dialog,
                    null,
                    false
            );

            new AlertDialog.Builder(getContext())
                    .setView(smsDialogBinding.getRoot())
                    .setPositiveButton("Send SMS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!smsDialogBinding.smsDestination.getText().toString().isEmpty()){
                                smsInfo.to = smsDialogBinding.smsDestination.getText().toString();
                                sendSms(smsInfo);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            sendSmsStarted = false;

            smsDialogBinding.setSmsInfo(smsInfo);

        }
    }

    private void sendSms(SmsInfo smsInfo){
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsInfo.to, null, smsInfo.text, pendingIntent, null);
    }

    private void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this Dog Breed");
        intent.putExtra(Intent.EXTRA_TEXT, currentDog.dogBreed +"Bred For : "+ currentDog.breedFor);
        intent.putExtra(Intent.EXTRA_STREAM, currentDog.imageUrl);
        startActivity(Intent.createChooser(intent, "Share With"));
    }

}
