package edu.stanford.cs108.rabbit;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.app.Activity;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThomasChen on 2/26/17.
 */



public class InventoryView extends View {
    List<Shape>  inventoryShapes;
    public InventoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inventoryShapes = new ArrayList<>();
//        Shape img = new Shape("carrot", "", "", "","");
//        img.setRectF(200, 0, 100, 200);
//        inventoryShapes.add(img);
        initBG();
    }

    BitmapDrawable inventoryBGDrawable;
    Bitmap inventoryBGBitmap;

    private void initBG() {
        inventoryBGDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.inventory_bg);
        inventoryBGBitmap = inventoryBGDrawable.getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(inventoryBGBitmap, null, new RectF(0,0, 1196, 200), null);

        for (Shape shape : inventoryShapes) {
            if (!shape.hidden) {
                shape.draw(canvas);
                //System.out.println(shape.rectF.toString());
            }

        }
    }

    float viewWidth, viewHeight;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    public float getViewHeight() {
        return viewHeight;
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public void addShape(Shape shape) {
        inventoryShapes.add(shape);
    }

    public void removeShape(Shape shape) {
        inventoryShapes.remove(shape);
    }

    public List<Shape> getInventoryShapes() {
        return inventoryShapes;
    }
}
