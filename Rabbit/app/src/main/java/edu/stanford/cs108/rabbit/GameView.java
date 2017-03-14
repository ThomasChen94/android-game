package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by qianyu on 2017/3/3.
 */

public class GameView extends View {

    static Page currPage;
    GameDatabase gameDatabase;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        gameDatabase = GameDatabase.getInstance();
        gameDatabase.getDb(context);
        //currPage = gameDatabase.getPage(1);

        Shape.setContext(context);


        currPage = gameDatabase.getPage("3"); // get the first page

        ViewTreeObserver vto = this.getViewTreeObserver();

/*        ViewTreeObserver vto = this.getViewTreeObserver();
>>>>>>> origin/master
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                GameView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                int height = GameView.this.getMeasuredHeight();
                int width  = GameView.this.getMeasuredWidth();
                Shape.setViewHeight(height);
                Shape.setViewWidth(width);
                //System.out.println(height + " " + width);
                return true;
            }
        });*/

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        System.out.println("width: " + size.x + " height: " + size.y);
        Shape.setViewHeight(size.y);
        Shape.setViewWidth(size.x);
        currPage = gameDatabase.getPage("3"); // get the first page
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //System.out.println(getWidth() + " " + getHeight());
        currPage.draw(canvas);
    }

    public void setCurrentPage(Page newPage) {
        currPage = newPage;
        invalidate(); //Once the page is changed, redraw the view.
    }

    public Page getCurrPage() {
        return currPage;
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
}
