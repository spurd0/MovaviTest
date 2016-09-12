package com.babenko.movavitest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.babenko.movavitest.data.Codes;
import com.babenko.movavitest.editors.ImageEditor;
import com.babenko.movavitest.fragments.ImageEditorFragment;
import com.babenko.movavitest.fragments.SelectPictureFragment;
import com.babenko.movavitest.helpers.UtilsHelper;
import com.babenko.movavitest.interfaces.EditPictureInterface;
import com.babenko.movavitest.interfaces.SelectPictureInterface;

import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity implements SelectPictureInterface, EditPictureInterface {
    private static final String TAG = "MainActivity";
    private ImageEditorFragment mImageEditorFragment;
    private String mImagePath;
    private static final String IMAGE_PATH_KEY = "imagePath";
    private ImageEditor mImageEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            showSelectPictureFragment();
        } else {
            mImagePath = savedInstanceState.getString(IMAGE_PATH_KEY);
            if (mImagePath != null) mImageEditor = new ImageEditor(this, mImagePath, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: { /// READ_EXTERNAL_STORAGE_CODE
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    UtilsHelper.makeToast(this, getResources().getString(R.string.no_permission_read_storage));
                }
            }
        }
    }

    @Override
    public void openGallery() {
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
                mImagePath = cursor.getString(columnIndex);
                cursor.close();
                Log.d(TAG, mImagePath);
                mImageEditor = new ImageEditor(this, mImagePath, this);
                showImageEditorFragment();
            }
        }
    }

    private void showSelectPictureFragment() {
        SelectPictureFragment selectFragment = new SelectPictureFragment();
        selectFragment.setmInterface(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, selectFragment)
                .commit();
    }

    private void showImageEditorFragment() {
        mImageEditorFragment = new ImageEditorFragment();

        getFragmentManager().beginTransaction()
                .addToBackStack("Selector")
                .replace(R.id.mainLayout, mImageEditorFragment)
                .commit();
    }

    @Override
    public void beforeButtonPressed() {
        mImageEditorFragment.setImage(mImageEditor.getOriginalImage());
    }

    @Override
    public void effectButtonPressed() {
        mImageEditorFragment.setImage(mImageEditor.getEditPreviewImage());
    }

    @Override
    public void afterButtonPressed() {
        mImageEditorFragment.setImage(mImageEditor.getEditedImage());
    }

    @Override
    public void changeSaturation(int position) {
        float sat = (float) 1 / position;
        mImageEditor.setSaturation(sat);
    }

    @Override
    public void imageEditorInited(int id) {
        if (getFragmentManager().findFragmentById(id) != null) {
            mImageEditorFragment = (ImageEditorFragment) getFragmentManager().findFragmentById(id);
        } else throw new NoSuchElementException("ImageEditorFragment couldn`t be founded");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_PATH_KEY, mImagePath);
    }
}
