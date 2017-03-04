package edu.stanford.cs108.rabbit;

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

/**
 * Created by ThomasChen on 2/26/17.
 */




public class displayView extends View {
    public displayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ImageShape img = new ImageShape("carrot", "", "");
        img.draw(canvas, getContext());
    }
}
