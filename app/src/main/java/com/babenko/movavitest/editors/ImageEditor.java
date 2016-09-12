package com.babenko.movavitest.editors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.babenko.movavitest.data.EditorState;
import com.babenko.movavitest.data.EditorState.editorState;
import com.babenko.movavitest.interfaces.EditPictureInterface;
import com.babenko.movavitest.R;

/**
 * Created by Roman Babenko (rbab@yandex.ru) on 9/1/2016.
 */
public class ImageEditor {
    private Bitmap mEditedBitmap;
    private Bitmap mOrigImageBitmap;
    private Bitmap mOriginalImagePartBm;
    private Bitmap mEditedImagePartBm;

    private Canvas mEditCanvas;

    private Paint mEffectPaint;
    private Paint mLinePaint;

    private Activity mActivity;
    private String mImagePath;

    private int mImageWidth;

    EditPictureInterface mInterface;

    public ImageEditor(Activity mActivity, String mImagePath, EditPictureInterface mInterface) {
        this.mActivity = mActivity;
        this.mImagePath = mImagePath;
        this.mInterface = mInterface;
        prepareEditor();
    }

    private void prepareEditor() {
        mOrigImageBitmap = loadResizedImage();
        mImageWidth = mOrigImageBitmap.getWidth();
        int imageHeight = mOrigImageBitmap.getHeight();
        mEditedBitmap = Bitmap.createBitmap(mImageWidth, imageHeight, mOrigImageBitmap.getConfig());
        mOriginalImagePartBm = Bitmap.createBitmap(mOrigImageBitmap, 0, 0, mImageWidth /2, imageHeight);
        mEditedImagePartBm = Bitmap.createBitmap(mOrigImageBitmap, mImageWidth /2, 0, mImageWidth /2, imageHeight);
        mEditCanvas = new Canvas(mEditedBitmap);
        mEffectPaint = new Paint();
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(10);
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
        EditorState.setState(editorState.preview);
        mEditCanvas.drawBitmap(mOriginalImagePartBm, 0, 0, mLinePaint);
        mEditCanvas.drawLine(mEditedBitmap.getWidth()/2, mEditedBitmap.getHeight(), mEditedBitmap.getWidth()/2, 0, mLinePaint);
        mEditCanvas.drawBitmap(mEditedImagePartBm, mImageWidth /2, 0, mEffectPaint);
        return mEditedBitmap;
    }

    public Bitmap getEditedImage() {
        EditorState.setState(editorState.after);
        mEditCanvas.drawBitmap(mOrigImageBitmap, 0, 0, mEffectPaint);
        return mEditedBitmap;
    }

    public Bitmap getOriginalImage() {
        EditorState.setState(editorState.before);
        return mOrigImageBitmap;
    }

    public void setSaturation(float saturation) {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(saturation);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        mEffectPaint.setColorFilter(f);

        switch (EditorState.getState()) {
            case preview: {
                mInterface.effectButtonPressed();
                break;
            }
            case after: {
                mInterface.afterButtonPressed();
                break;
            }
        }
    }
}
