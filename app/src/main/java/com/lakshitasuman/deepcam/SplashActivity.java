package com.lakshitasuman.deepcam;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.lakshitasuman.deepcam.permission.SampleActivity;

import vocsy.ads.GetSmartAdmob;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write whatever to want to do after delay specified (1 sec)
                if (checkWriteExternalPermission()) {
                    Intent intent = new Intent(SplashActivity.this, AugsMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, SampleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);

        String[] adsUrls = new String[]{
                getString(R.string.bnr_admob)// 1st Banner Ads Id
                , getString(R.string.native_admob)// 2st Native Ads Id
                , getString(R.string.int_admob)// 3st interstitial Ads Id
                , getString(R.string.app_open_admob)// 4st App-Open Ads Id
                , getString(R.string.video_admob)// 5st Rewarded Ads Id
        };

        new GetSmartAdmob(this, adsUrls, (success) -> {
            // admob init Success
        }).execute();
    }

    private boolean checkWriteExternalPermission() {
        String permission = Manifest.permission.CAMERA;
        String permission1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = checkCallingOrSelfPermission(permission);
        int res1 = checkCallingOrSelfPermission(permission1);
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return (res == PackageManager.PERMISSION_GRANTED);
        } else {
            return (res == PackageManager.PERMISSION_GRANTED && res1 == PackageManager.PERMISSION_GRANTED);
        }
    }
}