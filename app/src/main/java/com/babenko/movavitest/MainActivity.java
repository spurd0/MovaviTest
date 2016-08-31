package com.babenko.movavitest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.babenko.movavitest.Data.Codes;
import com.babenko.movavitest.Helpers.UtilsHelper;

public class MainActivity extends AppCompatActivity {

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
                    openGalery();
                } else {
                    UtilsHelper.requestPermission(activity, permission, Codes.READ_EXTERNAL_STORAGE_CODE);
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
                    openGalery();
                } else {
                    UtilsHelper.makeToast(this, getResources().getString(R.string.no_permission_read_storage));
                }
            }
        }
    }

    private void openGalery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Codes.PHOTO_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Codes.PHOTO_GALLERY) {
                Uri uri = data.getData();
                String[] fileColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, fileColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(fileColumn[0]);
                String photoPath = cursor.getString(columnIndex);
                cursor.close();
                //startEditing
            }
        }
    }
}
