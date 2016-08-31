package com.babenko.movavitest.Fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.babenko.movavitest.Interfaces.EditPictureInterface;
import com.babenko.movavitest.MainActivity;
import com.babenko.movavitest.R;

/**
 * Created by Roman Babenko (roman.babenko@sibers.com) on 8/31/2016.
 */
public class ImageEditorFragment extends Fragment {
    EditPictureInterface mInterface;
    ImageView mImage;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        ((MainActivity) getActivity()).onImageEditorFragmentCreated(this); // todo remake to event
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_editor_fragment, container, false);
    }

    private void initViews() {
        mImage = (ImageView) getView().findViewById(R.id.imageViewEditor);
    }

    public void setmInterface(EditPictureInterface mInterface) {
        this.mInterface = mInterface;
    }

    public void setImage(Bitmap mBitmap) {
        mImage.setImageBitmap(mBitmap);
    }
}
