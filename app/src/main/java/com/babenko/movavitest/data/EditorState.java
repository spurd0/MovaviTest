package com.babenko.movavitest.data;

/**
 * Created by Roman Babenko (rbab@yandex.ru) on 9/1/2016.
 */
public class EditorState {
    public enum editorState {before, preview, after}

    private static editorState state = editorState.preview;

    public static editorState getState() {
        return state;
    }

    public static void setState(editorState state) {
        EditorState.state = state;
    }
}
