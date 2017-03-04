package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by EricX on 2/3/17.
 */

public class DisplayView extends View {

    Page currPage;


<<<<<<< HEAD:Rabbit/app/src/main/java/edu/stanford/cs108/rabbit/InventoryView.java
public class InventoryView extends View {
    public InventoryView(Context context, AttributeSet attrs) {
=======

    public DisplayView(Context context, AttributeSet attrs) {
>>>>>>> origin/master:Rabbit/app/src/main/java/edu/stanford/cs108/rabbit/displayView.java
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ImageShape img = new ImageShape("carrot", "", "");
        img.draw(canvas, getContext());
    }

    public void setCurrentPage(Page newPage) {
        this.currPage = newPage;
        invalidate(); //Once the page is changed, redraw the view.
    }
}