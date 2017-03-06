package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qianyu on 2017/3/4.
 */

public class EditView extends View {
    // 在选择show的action的时候来存当前选择shape的名字
    String togoPageSelected;
    List<String> pageList;
    List<Shape> shapeList;
    Shape curShape;
    int curShapeIndex;
    boolean isClick;
    boolean isDrag;
    float relativeX;
    float relativeY;
    PopupWindow popupWindowMain;
    PopupWindow popupWindowAttribute;
    PopupWindow popupWindowScript;
    PopupWindow popupWindowAction;
    String shapeDefaultPrefix = "Shape";

    public void insertShape(String image, String text, String name, String page) {
        shapeList.add(new Shape(image, text, name, page));

    }

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        togoPageSelected = "";
        shapeList = new LinkedList<>();
        pageList = new LinkedList<>();
        Shape.setContext(context);
        initPopupWindowMain();
        initPopupWindowAttribute();
        initPopupWindowScript();
        initPopupWindowAction();
//        initPopupWindow(popupWindowMain, R.layout.popupwindow_main);
//        initPopupWindow(popupWindowAttribute, R.layout.popupwindow_attributes);
//        initPopupWindow(popupWindowScript, R.layout.popupwindow_script);
//        initPopupWindow(popupWindowAction, R.layout.popupwindow_action);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Shape shape : shapeList) {
            shape.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downEventHandler(event);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                dragEventHandler(event);
                break;
            }
            case MotionEvent.ACTION_UP: {
                upEventHandler(event);
                break;
            }
        }
        return true;
    }


    public Shape findShape(float downX, float downY) {
        for (int i = shapeList.size() - 1; i >= 0; i--) {
            if (shapeList.get(i).getRectF().contains(downX, downY)) {
                curShapeIndex = i;
                return shapeList.get(i);
            }
        }
        return null;
    }



    public void downEventHandler(MotionEvent event) {
        isClick = true;
        float downX = event.getX();
        float downY = event.getY();
        HorizontalScrollView hsv = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.hsv);
        hsv.setVisibility(View.INVISIBLE);
        String curPos = "x: " + downX + " y: " +downY;
        System.out.println(curPos);
        curShape = findShape(downX, downY);
        if (curShape != null) {
            System.out.println(getLastShape().getRectF().left + " " + getLastShape().getRectF().top + " " + getLastShape().getRectF().right + " " + getLastShape().getRectF().bottom);
            if (!curShape.getText().isEmpty()) {
                relativeX = downX - curShape.getRectF().left;
                relativeY = downY - curShape.getRectF().bottom;
            } else {
                relativeX = downX - curShape.getRectF().left;
                relativeY = downY - curShape.getRectF().top;
            }

        }

    }
    public void dragEventHandler(MotionEvent event) {
        isClick = false;
        isDrag = true;
        float downX = event.getX();
        float downY = event.getY();
        if (curShape != null) {
            if (!curShape.getText().isEmpty()) {
                float rectX = downX - relativeX;
                float rectY = downY - relativeY;
                curShape.setRectFLeftBottom(rectX, rectY);
            } else {
                float rectX = downX - relativeX;
                float rectY = downY - relativeY;
                curShape.setRectFLeftTop(rectX, rectY);
            }

        }
        invalidate();
    }
    public void upEventHandler(MotionEvent event) {
        if (isClick && curShape != null) {
            popupWindowMain.showAsDropDown(((Activity) getContext()).findViewById(R.id.insert_shape));
        }
    }
    public void initPopupWindow(PopupWindow popupWindow, int layout) {
        View popupView = LayoutInflater.from(getContext()).inflate(layout, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(popupView);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setAnimationStyle(R.style.AnimationFade);
    }
    public void initPopupWindowMain() {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_main, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowMain = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindowMain.setContentView(popupView);
        popupWindowMain.setTouchable(true);
        popupWindowMain.setOutsideTouchable(true);
        popupWindowMain.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindowMain.setAnimationStyle(R.style.AnimationFade);
    }

    public void initPopupWindowAttribute() {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_attributes, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowAttribute = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindowAttribute.setContentView(popupView);
        popupWindowAttribute.setTouchable(true);
        popupWindowAttribute.setOutsideTouchable(true);
        popupWindowAttribute.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindowAttribute.setAnimationStyle(R.style.AnimationFade);
    }

    public void initPopupWindowScript() {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_script, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowScript = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindowScript.setContentView(popupView);
        popupWindowScript.setTouchable(true);
        popupWindowScript.setOutsideTouchable(true);
        popupWindowScript.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindowScript.setAnimationStyle(R.style.AnimationFade);
    }

    public void initPopupWindowAction() {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_action, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowAction = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindowAction.setContentView(popupView);
        popupWindowAction.setTouchable(true);
        popupWindowAction.setOutsideTouchable(true);
        popupWindowAction.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindowAction.setAnimationStyle(R.style.AnimationFade);
    }
    public Shape getCurShape() {
        return curShape;
    }

    public int getCurShapeIndex() {
        return curShapeIndex;
    }

    public void setCurShape(Shape curShape) {
        this.curShape = curShape;
    }

    public void setCurShapeIndex() {
        shapeList.remove(curShapeIndex);
        invalidate();
    }

    public void expandAttributeMenu() {
        int width = popupWindowMain.getContentView().getMeasuredWidth();
        popupWindowAttribute.showAsDropDown(((Activity) getContext()).findViewById(R.id.insert_shape), width, 0);
    }


    public void expandScriptMenu() {
        int width = popupWindowMain.getContentView().getMeasuredWidth();
        popupWindowScript.showAsDropDown(((Activity) getContext()).findViewById(R.id.insert_shape), width, 0);
    }

    public Shape getLastShape() {
        return shapeList.get(shapeList.size() - 1);
    }

    public int getShapeCount() {
        return shapeList.size();
    }

    public void expandOnclickMenu() {
        int widthMain = popupWindowMain.getContentView().getMeasuredWidth();
        int widthScript = popupWindowScript.getContentView().getMeasuredWidth();
        popupWindowAction.showAsDropDown(((Activity) getContext()).findViewById(R.id.insert_shape), widthMain + widthScript, 0);
    }

    public void expandOnenterMenu() {
        int widthMain = popupWindowMain.getContentView().getMeasuredWidth();
        int widthScript = popupWindowScript.getContentView().getMeasuredWidth();
        popupWindowAction.showAsDropDown(((Activity) getContext()).findViewById(R.id.insert_shape), widthMain + widthScript, 0);
    }

    public void expandOndropMenu() {
        int widthMain = popupWindowMain.getContentView().getMeasuredWidth();
        int widthScript = popupWindowScript.getContentView().getMeasuredWidth();
        popupWindowAction.showAsDropDown(((Activity) getContext()).findViewById(R.id.insert_shape), widthMain + widthScript, 0);
    }

    public void showShapeScript() {
        System.out.println(curShape.getRawScript());
    }
}
