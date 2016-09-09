package com.example.hp.demouplayout.entities;

import android.graphics.drawable.Drawable;

/**
 * Created by kelvin on 07/09/16.
 */
public class OldCategory {

    int id;
    String title;
    Drawable drawable;

    public OldCategory(int id, Drawable drawable, String title){

        this.drawable = drawable;
        this.title = title;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

}
