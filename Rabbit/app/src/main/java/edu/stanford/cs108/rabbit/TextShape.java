package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by ThomasChen on 2/26/17.
 */

public class TextShape extends Shape {

    public TextShape(String image, String text, String soundName) {
        super(image, text, soundName);
        //System.out.println(image);
    }

    public TextShape(String image, String text, String soundName, String script, int order, boolean hidden, boolean movable, int left, int top, int right, int bottom) {
        super(image, text, soundName, script, order, hidden, movable, left, top, right, bottom);
    }

    public void draw(Canvas canvas, Context context) {

    }
}
