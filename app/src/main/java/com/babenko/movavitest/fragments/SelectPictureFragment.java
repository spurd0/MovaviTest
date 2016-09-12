package com.babenko.movavitest.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.babenko.movavitest.data.Codes;
import com.babenko.movavitest.helpers.UtilsHelper;
import com.babenko.movavitest.interfaces.SelectPictureInterface;
import com.babenko.movavitest.R;

/**
 * Created by Roman Babenko (rbab@yandex.ru) on 8/31/2016.
 */
public class SelectPictureFragment extends Fragment {
    SelectPictureInterface mInterface;

    public static final String ACTION_IMAGE_SELECTOR_FRAGMENT = "com.babenko.movavitest.RESPONSE_SELECT_PICTURE";
    public static final String IMAGE_SELECTOR_FRAGMENT_ID = "IMAGE_SELECTOR_FRAGMENT_ID";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.select_picture_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendIdToActivity();
        initButton();
    }

    private void sendIdToActivity() {
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_IMAGE_SELECTOR_FRAGMENT);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(IMAGE_SELECTOR_FRAGMENT_ID, this.getId());
        getActivity().sendBroadcast(responseIntent);
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
