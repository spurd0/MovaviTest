package com.babenko.movavitest.Interfaces;

/**
 * Created by Roman Babenko (rbab@yandex.ru) on 8/31/2016.
 */
public interface EditPictureInterface {
    void beforeButtonPressed();
    void effectButtonPressed();
    void afterButtonPressed();
    void changeSaturation(int position);
}