package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by qianyu on 2017/3/4.
 */

public class EditView extends View {
    // 在选择show的action的时候来存当前选择shape的名字
    String[] tmpScript;
    String togoPageSelected;
    List<String> pageList;
    List<Shape> shapeList;
    List<String> gameList;
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

    String curPageName;
    String curGameName;
    GameDatabase gameDatabase;



    static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";
    static final String PACKAGENAME = "edu.stanford.cs108.rabbit";

    public void insertShape(String image, String text, String name, String uniqueName, String page) {
        shapeList.add(new Shape(image, text, name, uniqueName, page));

    }

    public void insertPage(String pageName) {
        pageList.add(pageName);
        String pageUniqueName = curGameName + "Page" + String.valueOf(pageList.size());
        // background picture and background music is ignored here
        gameDatabase.addPage(new Page("", "", null, pageName, pageUniqueName, curGameName));
        ((EditActivity)getContext()).updatePageList();
    }

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameDatabase = GameDatabase.getInstance();
        gameDatabase.getDb(context);
        createOrLoadGame();

        shapeList = new LinkedList<>();
        pageList = new LinkedList<>();
        gameList = new LinkedList<>();

        curGameName = "Game1";
        togoPageSelected = "";
        tmpScript = new String[4];
        resetTmpScript();

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

    public void createOrLoadGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Create or Load Game");
        builder.setTitle("");
        gameList = gameDatabase.getGameNameList();

        // Create Game
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                gameList = gameDatabase.getGameNameList();

                final EditText editTextPage = new EditText(getContext());
                final int newPageIndex = pageList.size() + 1;
                editTextPage.setText("Page" + newPageIndex);
                AlertDialog.Builder builderPage = new AlertDialog.Builder(getContext());
                builderPage.setTitle("Rename");
                builderPage.setView(editTextPage);
                builderPage.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertPage(editTextPage.getText().toString());
                        setCurPageName(editTextPage.getText().toString());
                    }
                });
                builderPage.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertPage("Page" + newPageIndex);
                        setCurPageName("Page" + newPageIndex);
                    }
                });
                builderPage.setCancelable(true);
                AlertDialog dialogPage = builderPage.create();
                dialogPage.setCanceledOnTouchOutside(false);
                dialogPage.show();



                final EditText editTextGame = new EditText(getContext());
                final String gameUniqueName = "Game" + String.valueOf(gameList.size() + 1);
                editTextGame.setText(gameUniqueName);
                AlertDialog.Builder builderGame = new AlertDialog.Builder(getContext());
                builderGame.setTitle("Rename");
                builderGame.setView(editTextGame);
                builderGame.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setCurGameName(editTextGame.getText().toString());
                        gameDatabase.addGame(gameUniqueName, editTextGame.getText().toString());
                    }
                });
                builderGame.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setCurPageName("Game" + 1);
                        gameDatabase.addGame(gameUniqueName, gameUniqueName);
                    }
                });
                builderGame.setCancelable(true);
                AlertDialog dialogGame = builderGame.create();
                dialogGame.setCanceledOnTouchOutside(false);
                dialogGame.show();

            }
        });
        // Load Game
        builder.setNegativeButton("Load", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameList = gameDatabase.getGameNameList();
                Integer[] gameImageID = new Integer[EditView.this.gameList.size()];
                for (int i = 0; i < gameList.size(); i++) {
                    // 暂时用萝卜图
                    gameImageID[i] = getResources().getIdentifier("carrot", DRAWABLE, PACKAGENAME);
                }
                String[] gameName = new String[gameList.size()];
                for (int i = 0; i < gameList.size(); i++) {
                    gameName[i] = gameList.get(i);
                }
                AlertDialog.Builder builderLoad = new AlertDialog.Builder(getContext());
                builderLoad.setTitle("Choose Game to Load");
                ListAdapter adapter = new ArrayAdapterWithIcon(getContext(), gameName, gameImageID);
                builderLoad.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderLoad.setCancelable(true);
                AlertDialog dialogLoad = builderLoad.create();
                dialogLoad.setCanceledOnTouchOutside(false);
                dialogLoad.show();
            }
        });
        AlertDialog dialogLoadOrCreate = builder.create();
        dialogLoadOrCreate.setCanceledOnTouchOutside(false);
        dialogLoadOrCreate.show();
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
        HorizontalScrollView hsvPage = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.hsv_page);
        hsvPage.setVisibility(View.INVISIBLE);
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
            //popupWindowMain.showAsDropDown(((Activity) getContext()).findViewById(R.id.insert_shape));
            popupWindowMain.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden),0,0);
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
        gameDatabase.deleteShape(curShape);
        shapeList.remove(curShapeIndex);
        curShape = null;
        invalidate();
    }

    public void expandAttributeMenu() {
        int width = popupWindowMain.getContentView().getMeasuredWidth();
        popupWindowAttribute.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden), width, 0);
        //popupWindowAttribute.showAtLocation(this, Gravity.LEFT, width, 0);

    }


    public void expandScriptMenu() {
        int width = popupWindowMain.getContentView().getMeasuredWidth();
        popupWindowScript.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden), width, 0);
        //popupWindowScript.showAtLocation(this, Gravity.LEFT, width, 0);
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
        popupWindowAction.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden), widthMain + widthScript, 0);
        //popupWindowAction.showAtLocation(this, Gravity.LEFT, widthMain + widthScript, 0);

    }

    public void expandOnenterMenu() {
        int widthMain = popupWindowMain.getContentView().getMeasuredWidth();
        int widthScript = popupWindowScript.getContentView().getMeasuredWidth();
        popupWindowAction.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden), widthMain + widthScript, 0);
        //popupWindowAction.showAtLocation(this, Gravity.LEFT, widthMain + widthScript, 0);

    }

    public void expandOndropMenu() {
        int widthMain = popupWindowMain.getContentView().getMeasuredWidth();
        int widthScript = popupWindowScript.getContentView().getMeasuredWidth();
        popupWindowAction.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden), widthMain + widthScript, 0);
        //popupWindowAction.showAtLocation(this, Gravity.LEFT, widthMain + widthScript, 0);
    }

    public void showShapeScript() {
        Toast toast = Toast.makeText(getContext(), curShape.getRawScript(), Toast.LENGTH_SHORT);
        toast.show();
        System.out.println(curShape.getRawScript());
    }

    public void flushTmpScriptToRawScript() {
        for (String s : tmpScript) {
            if (s.isEmpty()) continue;
            getCurShape().rawScript += s + " ";
        }
        //getCurShape().setScript(getCurShape().rawScript);
    }

    public void resetTmpScript() {
        for (int i = 0; i < tmpScript.length; i++) {
            tmpScript[i] = "";
        }
    }

    public String getCurPageName() {
        return curPageName;
    }

    public void setCurPageName(String curPageName) {
        this.curPageName = curPageName;
    }

    public String getCurGameName() {
        return curGameName;
    }

    public void setCurGameName(String curGameName) {
        this.curGameName = curGameName;
    }


    public void updateCurPage(Page newPage) {
        shapeList = newPage.shapeList;
        if (shapeList == null) shapeList = new LinkedList<>();
        curShape = null;
        curShapeIndex = -1;
        invalidate();
    }

    public void createNewEmptyPage() {
        shapeList = new LinkedList<>();
        curShape = null;
        curShapeIndex = -1;
        invalidate();
    }

    public void testReadDb() {
        List<Page> pageList = gameDatabase.getGame("game1");

    }
}
