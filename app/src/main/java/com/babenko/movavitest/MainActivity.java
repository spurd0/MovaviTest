package com.babenko.movavitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.babenko.movavitest.Data.Codes;
import com.babenko.movavitest.Editors.ImageEditor;
import com.babenko.movavitest.Fragments.ImageEditorFragment;
import com.babenko.movavitest.Fragments.SelectPictureFragment;
import com.babenko.movavitest.Helpers.UtilsHelper;
import com.babenko.movavitest.Interfaces.EditPictureInterface;
import com.babenko.movavitest.Interfaces.SelectPictureInterface;

public class MainActivity extends AppCompatActivity implements SelectPictureInterface, EditPictureInterface {
    String TAG = "MainActivity";
    SelectPictureFragment mSelectFragment;
    ImageEditorFragment mImageEditorFragment;
    String imagePath;
    String IMAGE_PATH_KEY = "imagePath";
    ImageEditor mImageEditor;
    private MyBroadcastReceiver mMyBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) { // TODO: 8/31/2016  remade when start fragment
            showSelectPictureFragment();
        } else {
            imagePath = savedInstanceState.getString(IMAGE_PATH_KEY);
            mImageEditor = new ImageEditor(this, imagePath, this);
        }
        registerReceiver();
    }

    private void registerReceiver(){
        mMyBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(
                ImageEditorFragment.ACTION_IMAGE_EDITOR_FRAGMENT);
        intentFilter.addAction(SelectPictureFragment.ACTION_IMAGE_SELECTOR_FRAGMENT);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mMyBroadcastReceiver, intentFilter);
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
                imagePath = cursor.getString(columnIndex);
                cursor.close();
                Log.d(TAG, imagePath);
                mImageEditor = new ImageEditor(this, imagePath, this);
                showImageEditorFragment();
            }
        }
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

        getFragmentManager().beginTransaction()
                .addToBackStack("Selector")
                .replace(R.id.mainLayout, mImageEditorFragment)
                .commit();
    }

    private void setImageEditorInterface() {
        Log.d(TAG, "setImageEditorInterface");
        mImageEditorFragment.setInterface(this);
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

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            switch (intent.getAction()) {
                case (ImageEditorFragment.ACTION_IMAGE_EDITOR_FRAGMENT): {
                    int id = intent.getIntExtra(ImageEditorFragment.IMAGE_EDITOR_FRAGMENT_ID, -1);
                    mImageEditorFragment = (ImageEditorFragment) getFragmentManager().findFragmentById(id);
                    if (mImageEditorFragment != null) setImageEditorInterface();
                    break;
                }
                case (SelectPictureFragment.ACTION_IMAGE_SELECTOR_FRAGMENT): {
                    int id = intent.getIntExtra(SelectPictureFragment.IMAGE_SELECTOR_FRAGMENT_ID, -1);
                    mSelectFragment = (SelectPictureFragment) getFragmentManager().findFragmentById(id);
                    if (mSelectFragment != null) setSelectPictureInterface();
                    break;
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMyBroadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_PATH_KEY, imagePath);
    }
}
