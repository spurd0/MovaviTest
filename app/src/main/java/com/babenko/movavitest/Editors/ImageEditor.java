package com.babenko.movavitest.Editors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.babenko.movavitest.R;

/**
 * Created by Roman Babenko (roman.babenko@sibers.com) on 9/1/2016.
 */
public class ImageEditor {
    Bitmap editedBitmap;
    Bitmap origImageBitmap;
    Bitmap originalImagePartBm;
    Bitmap editedImagePartBm;

    Canvas editCanvas;

    Paint effectPaint;
    Paint linePaint;

    Activity mActivity;
    String mImagePath;

    int imageWidth;
    int imageHeight;

    public ImageEditor(Activity mActivity, String mImagePath) {
        this.mActivity = mActivity;
        this.mImagePath = mImagePath;
        prepareEditor();
    }

    private void prepareEditor() {
        origImageBitmap = loadResizedImage();
        imageWidth = origImageBitmap.getWidth();
        imageHeight = origImageBitmap.getHeight();
        editedBitmap = Bitmap.createBitmap(imageWidth, imageHeight, origImageBitmap.getConfig());
        originalImagePartBm = Bitmap.createBitmap(origImageBitmap, 0, 0, imageWidth/2, imageHeight);
        editedImagePartBm = Bitmap.createBitmap(origImageBitmap, imageWidth/2, 0, imageWidth/2, imageHeight);
        editCanvas = new Canvas(editedBitmap);
        effectPaint = new Paint();
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(10);
    }


    private Bitmap loadResizedImage() {
        int maxSize = mActivity.getResources().getInteger(R.integer.max_size);
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
            BitmapFactory.Options resample = new BitmapFactory.Options();
            resample.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(mImagePath, resample);
        } else return BitmapFactory.decodeFile(mImagePath, null);
    }

    public Bitmap getEditPreviewImage() {
        editCanvas.drawBitmap(originalImagePartBm, 0, 0, linePaint);
        editCanvas.drawLine(editedBitmap.getWidth()/2, editedBitmap.getHeight(), editedBitmap.getWidth()/2, 0, linePaint);

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        effectPaint.setColorFilter(f);
        editCanvas.drawBitmap(editedImagePartBm, imageWidth/2, 0, effectPaint);
        return editedBitmap;
    }

    public Bitmap getEditedImage() {
        editCanvas.drawBitmap(origImageBitmap, 0, 0, effectPaint);
        return editedBitmap;
    }

    public Bitmap getOriginalImage() {
        return origImageBitmap;
    }
}