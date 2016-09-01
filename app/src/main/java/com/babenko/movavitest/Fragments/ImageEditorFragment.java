package com.babenko.movavitest.Fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.babenko.movavitest.Interfaces.EditPictureInterface;
import com.babenko.movavitest.MainActivity;
import com.babenko.movavitest.R;

import java.util.ArrayList;

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
        final Button beforeButt = (Button) getView().findViewById(R.id.buttonBefore);
        final Button effectButt = (Button) getView().findViewById(R.id.buttonEffects);
        final Button afterButt = (Button) getView().findViewById(R.id.buttonAfter);
        final ArrayList<Button> buttonsList = new ArrayList<Button>();
        buttonsList.add(beforeButt);
        buttonsList.add(effectButt);
        buttonsList.add(afterButt);

        effectButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsPressed(effectButt, buttonsList);
                mInterface.effectButtonPressed();
            }
        });

        beforeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsPressed(beforeButt, buttonsList);
                mInterface.beforeButtonPressed();
            }
        });

        afterButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsPressed(afterButt, buttonsList);
                mInterface.afterButtonPressed();
            }
        });
    }

    public void setmInterface(EditPictureInterface mInterface) {
        this.mInterface = mInterface;
    }

    public void setImage(Bitmap mBitmap) {
        mImage.setImageBitmap(mBitmap);
    }

    private void setButtonsPressed(Button button, ArrayList<Button> list) {
        for (Button but : list) {
            if (but == button) {
                but.setBackgroundResource(R.drawable.button_state_pressed);
            } else {
                but.setBackgroundResource(android.R.drawable.btn_default);
            }
        }
    }
}
