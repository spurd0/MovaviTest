package com.babenko.movavitest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import com.babenko.movavitest.Interfaces.EditPictureInterface;
import com.babenko.movavitest.Interfaces.SelectPictureInterface;

public class MainActivity extends AppCompatActivity implements SelectPictureInterface, EditPictureInterface {
    String TAG = "MainActivity";
    SelectPictureFragment mSelectFragment;
    ImageEditorFragment mImageEditorFragment;
    String imagePath;
    Bitmap editedBitmap;

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
                imagePath = cursor.getString(columnIndex);
                cursor.close();
                Log.d(TAG, imagePath);
                showImageEditorFragment();
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
                .replace(R.id.mainLayout, mImageEditorFragment)
                .commit();
    }

    public void onImageEditorFragmentCreated(ImageEditorFragment imageEditorFragment) {
        this.mImageEditorFragment = imageEditorFragment;
        setImageEditorInterface();
        mImageEditorFragment.setImage(loadResizedImage(imagePath));
    }

    private void setImageEditorInterface() {
        mImageEditorFragment.setmInterface(this);
    }

    private Bitmap loadResizedImage(String mImagePath) {
        int maxSize = getResources().getInteger(R.integer.max_size);
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImagePath, bounds);
        int width = bounds.outWidth;
        int height = bounds.outHeight;
        boolean withinBounds = width <=maxSize  && height <= maxSize;
        if (!withinBounds) {
            float sampleSizeF;
            if (width >= height) {
                sampleSizeF = (float) width / (float) maxSize;
            } else {
                sampleSizeF = (float) height / (float) maxSize;
            }
            int sampleSize = Math.round(sampleSizeF);
            Log.d(TAG, "loading " + width + " " + height + " " + sampleSizeF + " " + sampleSize);
            BitmapFactory.Options resample = new BitmapFactory.Options();
            resample.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(mImagePath, resample);
        } else return BitmapFactory.decodeFile(mImagePath, null);
    }

    @Override
    public void beforeButtonPressed() {
        mImageEditorFragment.setImage(loadResizedImage(imagePath));
    }

    @Override
    public void effectButtonPressed() {
        mImageEditorFragment.setImage(loadEffectImage(imagePath));
    }

    @Override
    public void afterButtonPressed() {

    }

    private Bitmap loadEffectImage(String mImagePath) {
        Bitmap image = loadResizedImage(mImagePath);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        editedBitmap = Bitmap.createBitmap(imageWidth, imageHeight, image.getConfig());
        Bitmap originalImagePartBm = Bitmap.createBitmap(image, 0, 0, imageWidth/2, imageHeight);
        Bitmap editedImagePartBm = Bitmap.createBitmap(image, imageWidth/2, 0, imageWidth/2, imageHeight);
        Canvas editCanvas = new Canvas(editedBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);

        editCanvas.drawBitmap(originalImagePartBm, 0, 0, paint);
        editCanvas.drawLine(editedBitmap.getWidth()/2, editedBitmap.getHeight(), editedBitmap.getWidth()/2, 0, paint);


        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        editCanvas.drawBitmap(editedImagePartBm, imageWidth/2, 0, paint);
        return editedBitmap;
    }


}
