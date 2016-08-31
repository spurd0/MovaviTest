package com.babenko.movavitest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.babenko.movavitest.Data.Codes;
import com.babenko.movavitest.Fragments.ImageEditorFragment;
import com.babenko.movavitest.Fragments.SelectPictureFragment;
import com.babenko.movavitest.Helpers.UtilsHelper;
import com.babenko.movavitest.Interfaces.SelectPictureInterface;

public class MainActivity extends AppCompatActivity implements SelectPictureInterface {
    String TAG = "MainActivity";
    SelectPictureFragment mSelectFragment;
    ImageEditorFragment mImageEditorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) { // TODO: 8/31/2016  remade when start fragment
            showSelectPictureFragment();
        }
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

    @Override
    public void openGalery() {
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
                Log.d(TAG, photoPath);
            }
        }
    }

    public void onSelectPictureFragmentCreated(SelectPictureFragment selectFragment) {
        this.mSelectFragment = selectFragment;
        setSelectPictureInterface();
    }

    private void setSelectPictureInterface() {
        mSelectFragment.setmInterface(this);
    }

    private void showSelectPictureFragment() {
        mSelectFragment = new SelectPictureFragment();
        mSelectFragment.setmInterface(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, mSelectFragment)
                .commit();
    }

    private void showImageEditorFragment() {
        mImageEditorFragment = new ImageEditorFragment();
        mImageEditorFragment.setmInterface(this);

        getFragmentManager().beginTransaction()
                .addToBackStack("Selector")
                .replace(R.id.mainLayout, mSelectFragment)
                .commit();
    }

}
