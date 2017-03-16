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


    String uniqueName;
    String name;
    String game;
    protected List<Shape> shapeList;

    protected MediaPlayer mp;

    protected static final String PACKAGE_NAME = "edu.stanford.cs108.rabbit";
    protected static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";


    public String getUniqueName() {
        return uniqueName;
    }

    public String getSoundName() {
        return soundName;
    }

    public String getBackground() {
        return background;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Page(String background, String soundName, List<Shape> shapeList, String pageName, String pageUniqueName, String gameName) {
        this.background = background;
        this.soundName = soundName;
        this.shapeList = shapeList;
        this.name = pageName;
        this.uniqueName = pageUniqueName;
        this.game = gameName;
    }

    public void draw(Canvas canvas) {
        for (Shape shape : shapeList) {
            if (!shape.hidden) {
                shape.setRectF();
                shape.draw(canvas);
            }
        }
    }

    public void playSound(View view, Context context) {
        int soundId = context.getResources().getIdentifier(soundName, RAW, context.getPackageName());
        mp = MediaPlayer.create(view.getContext(), soundId);
        mp.start();
    }

    public List<Shape> getShapeList() {
        return shapeList;
    }

    public String getGame() {
        return game;
    }
}
