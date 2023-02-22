package com.lakshitasuman.deepcam.permission;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lakshitasuman.deepcam.AugsMainActivity;
import com.lakshitasuman.deepcam.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class SampleActivity extends Activity {

    Button all_permissions_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);

        all_permissions_button = (Button) findViewById(R.id.all_permissions_button);

        if (checkWriteExternalPermission()) {
            startActivity(new Intent(SampleActivity.this, AugsMainActivity.class));
        } else {
            permission();
        }
    }


    void permission() {

        all_permissions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    String[] permissions = {Manifest.permission.CAMERA};

                    String rationale = "Please provide permission so that you can ...";
                    Permissions.Options options = new Permissions.Options()
                            .setRationaleDialogTitle("Info")
                            .setSettingsDialogTitle("Warning");

                    Permissions.check(SampleActivity.this, permissions, rationale, options, new PermissionHandler() {
                        @Override
                        public void onGranted() {

                            startActivity(new Intent(SampleActivity.this, AugsMainActivity.class));
                        }

                        @Override
                        public void onDenied(Context context, ArrayList<String> deniedPermissions) {

                        }
                    });
                } else {
                    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    String rationale = "Please provide permission so that you can ...";
                    Permissions.Options options = new Permissions.Options()
                            .setRationaleDialogTitle("Info")
                            .setSettingsDialogTitle("Warning");

                    Permissions.check(SampleActivity.this, permissions, rationale, options, new PermissionHandler() {
                        @Override
                        public void onGranted() {

                            startActivity(new Intent(SampleActivity.this, AugsMainActivity.class));
                        }

                        @Override
                        public void onDenied(Context context, ArrayList<String> deniedPermissions) {

                        }
                    });
                }


            }
        });
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
