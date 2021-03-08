package com.example.jetpackmasterclass.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.jetpackmasterclass.R;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_SEND_SMS = 342;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);

    }

    public void checkSmsPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                new AlertDialog.Builder(this)
                        .setTitle("Send SMS Permission")
                        .setMessage("This app requires access to send an SMS")
                        .setPositiveButton("Ask Me", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSmsPermission();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notifyDetailsFragment(false);
                            }
                        })
                        .show();
            }else{
                requestSmsPermission();
            }
        }else{
            notifyDetailsFragment(true);
        }
    }
    private void requestSmsPermission(){
        String[] permissions = {Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_SEND_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_SEND_SMS: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    notifyDetailsFragment(true);
                }else{
                    notifyDetailsFragment(false);
                }
                break;
            }
        }
    }

    private void notifyDetailsFragment(boolean permissionGranted){
        Fragment activeFragment = fragment.getChildFragmentManager().getPrimaryNavigationFragment();
        if(activeFragment instanceof DetailsFragment){
            ((DetailsFragment) activeFragment).onPermissionResult(permissionGranted);
        }
    }

}
