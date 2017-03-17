package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Point;
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
        //currPage = gameDatabase.getPage(1);

        Shape.setContext(context);

        //currPage = gameDatabase.getPage("3"); // get the first page

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //System.out.println("width: " + size.x + " height: " + size.y);
        Shape.setGameView(this);
        Shape.setViewHeight(size.y);
        Shape.setViewWidth(size.x);
        // currPage = gameDatabase.getPage("Game1Page1"); // get the first page
        //System.out.print("");

        pageChanged = true;//for first page on enter actions
    }

boolean loaded = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //System.out.println(getWidth() + " " + getHeight());
//        System.out.println("I am in onDraw!!!!!!" );
//        System.out.println("pageChanged!!!!!!" + pageChanged );

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
//        System.out.println("currPage unique name!!!!!!" + currPage.getUniqueName() );
//        System.out.println("newPage unique name!!!!!!" + newPage.getUniqueName() );

        pageChanged = true;  //this line should be commented out when the next line works
        if (!newPage.getUniqueName().equals(currPage.getUniqueName())) pageChanged = true; //TODO now both curr and new page's uniquename is "".
        currPage = newPage;
        invalidate(); //Once the page is changed, redraw the view.

    }
    int count = 0;

    private void processOnEnter() {
        count++;
        //System.out.println("I am in onEnter!!!!!!" + count);
        List<Shape> currPageShapeList= currPage.getShapeList();
        for (Shape shape : currPageShapeList) {
            List<Action> triggerActionList = shape.getTriggerActionList();
            for (Action action : triggerActionList) {
                if (action instanceof OnEnterAction) {
                    //System.out.println("found onEnter Action");
                    List<String> actionList = action.actionList;
                    for (String str : actionList) {
                        //System.out.println(str);

                        if (str.contains("GOTO")) {
                            //System.out.println("calling GOTO");

                            action.onGoto(str.trim().substring(5));
                        }
                        if (str.contains("SHOW")) {  //Tested; it is working
                            //System.out.println("calling SHOW");
                            action.onShow(str.trim().substring(5));
                            //action.onHide("page2_shape1");
                        }
                        if (str.contains("HIDE")) {
                            //System.out.println("calling HIDE");

                            action.onHide(str.trim().substring(5));
                        }
                        if (str.contains("PLAY")) {
                            //System.out.println("calling PLAY");
                            //System.out.println("The song is: " + str.trim().substring(5));
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
