package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.View;

import java.util.List;

/**
 * Created by qianyu on 2017/2/26.
 */

public class Page {
    protected String background;
    protected String soundName;
    protected List<Shape> shapeList;
    protected MediaPlayer mp;

    protected static final String PACKAGE_NAME = "edu.stanford.cs108.rabbit";
    protected static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";


    public Page() {

    }
    public void draw(Canvas canvas) {
        for (Shape shape : shapeList) {
            shape.draw(canvas);
        }
    }
    public void playSound(View view, Context context) {
        int soundId = context.getResources().getIdentifier(soundName, RAW, context.getPackageName());
        mp = MediaPlayer.create(view.getContext(), soundId);
        mp.start();
    }
}
