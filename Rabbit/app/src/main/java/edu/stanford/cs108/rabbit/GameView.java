package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianyu on 2017/3/3.
 */

public class GameView extends View {

    static Page currPage;
    GameDatabase gameDatabase;
    boolean pageChanged;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        gameDatabase = GameDatabase.getInstance();
        gameDatabase.getDb(context);

        Shape.setContext(context);

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Shape.setGameView(this);
        Shape.setViewHeight(size.y);
        Shape.setViewWidth(size.x);

        pageChanged = true;//for first page on enter actions
        initBG();
    }

    BitmapDrawable backgroundDrawable;
    Bitmap backgroundBitmap;

    private void initBG() {
        backgroundDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.grassland_bg);
        backgroundBitmap = backgroundDrawable.getBitmap();
    }

    boolean loaded = false;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(backgroundBitmap, null, new RectF(0,0, 1196, 768), null);

        if (pageChanged == true) {
            if (loaded) processOnEnter();
            pageChanged = false;
        }
        if (loaded) currPage.draw(canvas);

    }

    public static void initFirstPage(Page firstPage) {
        currPage = firstPage;
    }


    public void setCurrentPage(Page newPage) {
        if (!newPage.getUniqueName().equals(currPage.getUniqueName())) pageChanged = true;
        currPage = newPage;
        invalidate(); //Once the page is changed, redraw the view.

    }
    int count = 0;

    private void processOnEnter() {
        count++;
        List<Shape> currPageShapeList= currPage.getShapeList();
        for (Shape shape : currPageShapeList) {
            List<Action> triggerActionList = shape.getTriggerActionList();
            for (Action action : triggerActionList) {
                if (action instanceof OnEnterAction) {
                    List<String> actionList = action.actionList;
                    for (String str : actionList) {
                        if (str.contains("GOTO")) {
                            action.onGoto(str.trim().substring(5));
                        }
                        if (str.contains("SHOW")) {
                            action.onShow(str.trim().substring(5));
                        }
                        if (str.contains("HIDE")) {
                            action.onHide(str.trim().substring(5));
                        }
                        if (str.contains("PLAY")) {
                            action.onPlay(str.trim().substring(5));
                        }
                    }
                }
            }
        }
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
