package com.babenko.movavitest.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.babenko.movavitest.interfaces.EditPictureInterface;
import com.babenko.movavitest.R;

import java.util.ArrayList;

/**
 * Created by Roman Babenko (rbab@yandex.ru) on 8/31/2016.
 */
public class ImageEditorFragment extends Fragment {
    private EditPictureInterface mInterface;
    private ImageView mImage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if (context instanceof Activity){
            a = (Activity) context;
            try {
                mInterface = (EditPictureInterface) a;
                mInterface.imageEditorInited(this.getId());
            } catch (ClassCastException e) {
                throw new ClassCastException(a.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
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
        final SeekBar saturationSeek = (SeekBar) getView().findViewById(R.id.saturationSeekBar);
        saturationSeek.setMax(9);
        saturationSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mInterface.changeSaturation(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        buttonsList.add(beforeButt);
        buttonsList.add(effectButt);
        buttonsList.add(afterButt);

        effectButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewsEnabeled(effectButt, buttonsList, saturationSeek, true);
                Log.d("TAG", "setOnClickListener " + (mInterface == null));
                mInterface.effectButtonPressed();
            }
        });

        beforeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewsEnabeled(beforeButt, buttonsList, saturationSeek, false);
                mInterface.beforeButtonPressed();
            }
        });

        afterButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewsEnabeled(afterButt, buttonsList, saturationSeek, true);
                mInterface.afterButtonPressed();
            }
        });
        effectButt.performClick(); // // TODO: 9/1/2016 temp solution, rework
    }

    public void setImage(Bitmap mBitmap) {
        mImage.setImageBitmap(mBitmap);
    }

    private void setViewsEnabeled(Button button, ArrayList<Button> list, SeekBar seekBar,
                                  boolean sbEnabeled) {
        for (Button but : list) {
            if (but == button) {
                but.setBackgroundResource(R.drawable.button_state_pressed);
            } else {
                but.setBackgroundResource(android.R.drawable.btn_default);
            }
        }
        if (sbEnabeled) {
            seekBar.setVisibility(View.VISIBLE);
        } else seekBar.setVisibility(View.INVISIBLE);
    }
}
