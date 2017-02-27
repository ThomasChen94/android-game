package edu.stanford.cs108.rabbit;

import android.graphics.RectF;

import org.json.JSONObject;

/**
 * Created by qianyu on 2017/2/26.
 */

public abstract class Shape {
    // fields
    private String id;
    private int page;
    private RectF rectF;
    private JSONObject script;
    private String image;
    private String text;
    private boolean hidden;
    private boolean movable;
    private String sound;


    // methods
    public Shape() {

    }

    public void draw() {

    }

    public void playSound() {

    }
}
