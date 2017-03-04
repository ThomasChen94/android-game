package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * Created by qianyu on 2017/3/3.
 */

public class GameView extends View {

    static Page currPage;
    GameDatabase gameDatabase;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameDatabase = GameDatabase.getInstance();
        //currPage = gameDatabase.getPage(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //currPage.draw(canvas);
    }

    public void setCurrentPage(Page newPage) {
        currPage = newPage;
        invalidate(); //Once the page is changed, redraw the view.
    }
}
