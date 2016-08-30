package com.babenko.movavitest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.babenko.movavitest.Helpers.UtilsHelper;

public class MainActivity extends AppCompatActivity {
    private int READ_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButton();
    }

    private void initButton() {
        Button startButton = (Button) findViewById(R.id.startButton);
        final Activity activity = this;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                if (UtilsHelper.checkPermission(activity, permission)) {
                    // open galery chooser
                } else {
                    UtilsHelper.requestPermission(activity, permission, READ_EXTERNAL_STORAGE_CODE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: { /// READ_EXTERNAL_STORAGE_CODE
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPathFiles = Spugram.getInstance().getAppDir(this);
                    sendAuthRequest(mPathFiles);
                } else {
                    UtilsHelper.makeToast(this, getResources().getString(R.string.no_permission_read_storage));
                }
            }

        }
    }

}
