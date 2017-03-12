package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.util.AttributeSet;
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

        currPage = gameDatabase.getPage("1"); // get the first page

        ViewTreeObserver vto = this.getViewTreeObserver();
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
        });
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
