package com.babenko.movavitest.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.babenko.movavitest.Data.Codes;
import com.babenko.movavitest.Helpers.UtilsHelper;
import com.babenko.movavitest.Interfaces.SelectPictureInterface;
import com.babenko.movavitest.MainActivity;
import com.babenko.movavitest.R;

/**
 * Created by Roman Babenko (rbab@yandex.ru) on 8/31/2016.
 */
public class SelectPictureFragment extends Fragment {
    SelectPictureInterface mInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.select_picture_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initButton();
        ((MainActivity) getActivity()).onSelectPictureFragmentCreated(this); // todo remake to event
    }

    private void initButton() {
        Button startButton = (Button) getView().findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                if (UtilsHelper.checkPermission(getActivity(), permission)) {
                    mInterface.openGalery();
                } else {
                    UtilsHelper.requestPermission(getActivity(), permission, Codes.READ_EXTERNAL_STORAGE_CODE);
                }
            }
        });
    }

    public void setmInterface(SelectPictureInterface mInterface) {
        this.mInterface = mInterface;
    }
}
