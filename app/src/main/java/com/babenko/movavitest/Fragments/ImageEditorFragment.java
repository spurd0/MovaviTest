package com.babenko.movavitest.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.babenko.movavitest.Interfaces.EditPictureInterface;
import com.babenko.movavitest.MainActivity;
import com.babenko.movavitest.R;

/**
 * Created by Roman Babenko (roman.babenko@sibers.com) on 8/31/2016.
 */
public class ImageEditorFragment extends Fragment {
    EditPictureInterface mInterface;


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

    }

    public void setmInterface(EditPictureInterface mInterface) {
        this.mInterface = mInterface;
    }
}
