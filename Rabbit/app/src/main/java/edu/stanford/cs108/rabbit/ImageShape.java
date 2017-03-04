package edu.stanford.cs108.rabbit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.content.Context;
import android.graphics.RectF;

import java.io.InputStream;

/**
 * Created by ThomasChen on 2/26/17.
 */

public class ImageShape extends Shape {

    public ImageShape(String image, String text, String soundName) {
        super(image, text, soundName);
        System.out.println(image);
    }

    public ImageShape(String image, String text, String soundName, String script, int order, boolean hidden, boolean movable, int left, int top, int right, int bottom) {
        super(image, text, soundName, script, order, hidden, movable, left, top, right, bottom);
    }

    public void draw(Canvas canvas, Context context) {
        System.out.println(image);
        int imageId = context.getResources().getIdentifier(image, DRAWABLE, context.getPackageName());

        BitmapFactory.Options opts = new BitmapFactory.Options();

        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;

        //InputStream is = context.getResources().openRawResource(imageId);

        Bitmap imageDraw = BitmapFactory.decodeResource(context.getResources(), imageId, opts); // open the selected image
        canvas.drawBitmap(imageDraw, 0, 0, new Paint());

        System.out.println(imageDraw.getWidth() + " " + imageDraw.getHeight());
        rectF = new RectF(0, 0, imageDraw.getWidth(), imageDraw.getHeight());
    }
}
