package com.babenko.movavitest.Data;

/**
 * Created by Roman Babenko (roman.babenko@sibers.com) on 9/1/2016.
 */
public class EditorState {
    public static enum editorState {before, preview, after}

    private static editorState state = editorState.preview;

    public static editorState getState() {
        return state;
    }

    public static void setState(editorState state) {
        EditorState.state = state;
    }
}
