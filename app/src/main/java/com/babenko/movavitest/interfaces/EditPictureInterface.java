package com.babenko.movavitest.interfaces;

/**
 * Created by Roman Babenko (rbab@yandex.ru) on 8/31/2016.
 */
public interface EditPictureInterface {
    void beforeButtonPressed();
    void effectButtonPressed();
    void afterButtonPressed();
    void changeSaturation(int position);
    void imageEditorInited(int id);
}
