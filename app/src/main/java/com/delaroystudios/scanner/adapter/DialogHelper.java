package com.delaroystudios.scanner.adapter;

import android.graphics.drawable.Drawable;

public class DialogHelper {
    private String name;
    private Drawable flag;

    public DialogHelper(String name, Drawable flag){
        this.name = name;
        this.flag = flag;

    }
    public String getName() {
        return name;
    }

    public Drawable getFlag() {
        return flag;
    }
}
