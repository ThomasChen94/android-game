package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
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
    List<String> pageUserList;
    List<String> pageUniqueList;
    List<Shape> shapeList;
    List<String> gameList;
    Shape curShape;
    int curShapeIndex;
    boolean isClick;
    boolean isDrag;
    float relativeX;
    float relativeY;
    PopupWindow popupWindowMain;
    PopupWindow popupWindowPage;
    PopupWindow popupWindowAttribute;
    PopupWindow popupWindowScript;
    PopupWindow popupWindowAction;
    PopupWindow popupWindowSettings;
    String shapeDefaultPrefix = "Shape";

    int curPageIndex;
    String curPageName;
    String curGameName;
    GameDatabase gameDatabase;


    Switch hidden;
    Switch movable;
    Switch position;
    Switch pageSwitch;
    Switch uniquePageSwitch;
    SeekBar resizeSeekbar;
    static final String[] GAMEICONLIST = {"gba_icon", "controller_icon", "xbox_icon", "steam_icon", "playstation_icon"};
    static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";
    static final String PACKAGENAME = "edu.stanford.cs108.rabbit";

    public void insertShape(String image, String text, String name, String uniqueName, String page) {
        shapeList.add(new Shape(image, text, name, uniqueName, page));

    }

    public void insertPage(String pageName) {
        pageUserList.add(pageName);
        String pageUniqueName = curGameName + "Page" + String.valueOf(pageUserList.size());
        pageUniqueList.add(pageUniqueName);
        // background picture and background music is ignored here
        gameDatabase.addPage(new Page("", "", null, pageName, pageUniqueName, curGameName));

        ((EditActivity)getContext()).updatePageList();
        TextView textView1 = (TextView) ((Activity)getContext()).findViewById(R.id.page_textview);
        textView1.setText(curGameName + curPageName);
        TextView textView2 = (TextView) ((Activity)getContext()).findViewById(R.id.unique_page_textview);
        textView2.setText(pageUniqueList.get(curPageIndex));
    }

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameDatabase = GameDatabase.getInstance();
        gameDatabase.getDb(context);
        createOrLoadGame();

        shapeList = new LinkedList<>();
        pageUserList = new LinkedList<>();
        pageUniqueList = new LinkedList<>();
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
        initPopupWindowPage();
        initPopupWindowSettings();

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Shape.setViewHeight(size.y);
        Shape.setViewWidth(size.x);
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

//                final EditText editTextPage = new EditText(getContext());
//                final int newPageIndex = pageUserList.size() + 1;
//                editTextPage.setText("Page" + newPageIndex);
//                AlertDialog.Builder builderPage = new AlertDialog.Builder(getContext());
//                builderPage.setTitle("Rename");
//                builderPage.setView(editTextPage);
//                builderPage.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        insertPage(editTextPage.getText().toString());
//                        setCurPageName(editTextPage.getText().toString());
//                    }
//                });
//                builderPage.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        insertPage("Page" + newPageIndex);
//                        setCurPageName("Page" + newPageIndex);
//                    }
//                });
//                builderPage.setCancelable(true);
//                AlertDialog dialogPage = builderPage.create();
//                dialogPage.setCanceledOnTouchOutside(false);
//                dialogPage.show();

//                insertPage("Page1");
//                setCurPageName("Page1");

                final EditText editTextGame = new EditText(getContext());
                final String gameUniqueName = "Game" + String.valueOf(gameList.size() + 1);
                editTextGame.setText(gameUniqueName);
                AlertDialog.Builder builderGame = new AlertDialog.Builder(getContext());
                builderGame.setTitle("Rename");
                builderGame.setView(editTextGame);
                builderGame.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        setCurGameName(editTextGame.getText().toString());
//                        gameDatabase.addGame(gameUniqueName, editTextGame.getText().toString());
//                        insertPage("Page1");
//                        setCurPageName("Page1");
                    }
                });
                builderGame.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), FullscreenActivity.class);
                        ((EditActivity)getContext()).startActivity(intent);
                    }
                });
                builderGame.setCancelable(false);
                final AlertDialog dialogGame = builderGame.create();
                dialogGame.setCanceledOnTouchOutside(false);
                dialogGame.show();
                dialogGame.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String curNewGameName = editTextGame.getText().toString();
                        if (!gameDatabase.containsGame(curNewGameName)) {
                            setCurGameName(curNewGameName);
                            gameDatabase.addGame(gameUniqueName, curNewGameName);
                            insertPage("Page1");
                            setCurPageName("Page1");
                            dialogGame.dismiss();
                        } else {
                            final EditView editView = (EditView) findViewById(R.id.editView);
                            AlertDialog.Builder builderWarning = new AlertDialog.Builder(getContext());
                            builderWarning.setTitle("Duplicated Game Name");
                            builderWarning.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builderWarning.setCancelable(true);
                            AlertDialog dialogWarning = builderWarning.create();
                            dialogWarning.setCanceledOnTouchOutside(false);
                            dialogWarning.show();
                        }
                    }
                });

            }
        });
        // Load Game
        builder.setNegativeButton("Load", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameList = gameDatabase.getGameNameList();
                Integer[] gameImageID = new Integer[EditView.this.gameList.size()];
                for (int i = 0; i < gameList.size(); i++) {
                    gameImageID[i] = getResources().getIdentifier(GAMEICONLIST[i % GAMEICONLIST.length], DRAWABLE, PACKAGENAME);
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
                        List<Page> dummyPageList = gameDatabase.getGame(gameList.get(which));
                        setCurGameName(gameList.get(which));
                        pageUniqueList = new ArrayList<String>(dummyPageList.size());
                        pageUserList = new ArrayList<String>(dummyPageList.size());
                        for (int i = 0; i < dummyPageList.size(); i++) {
                            pageUniqueList.add(dummyPageList.get(i).getUniqueName());
                            pageUserList.add(dummyPageList.get(i).getName());
                        }
                        ((EditActivity)getContext()).resetPageList();
                        // Page newPage = gameDatabase.getPage(pageUniqueList.get(0));
                        Page newPage = gameDatabase.getPage(getCurGameName() + pageUserList.get(0));
                        setCurPageName(pageUserList.get(0));
                        updateCurPage(newPage);
                    }
                });
                builderLoad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), FullscreenActivity.class);
                        ((EditActivity)getContext()).startActivity(intent);
                    }
                });
                builderLoad.setCancelable(false);
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
        if (isClick && curShape != null) curShape.drawBorder(canvas);
        if (curShape != null) {
            TextView textView1 = (TextView) ((Activity)getContext()).findViewById(R.id.page_textview);
            textView1.setText(curGameName + curPageName + curShape.name);
            TextView textView2 = (TextView) ((Activity)getContext()).findViewById(R.id.unique_page_textview);
            textView2.setText(curShape.uniqueName);
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
        curShape = findShape(downX, downY);
        if (curShape != null) {
            if (!curShape.getText().isEmpty()) {
                relativeX = downX - curShape.getRectF().left;
                relativeY = downY - curShape.getRectF().bottom;
            } else {
                relativeX = downX - curShape.getRectF().left;
                relativeY = downY - curShape.getRectF().top;
            }

        }

        TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.position_textview);
        if (textView.getVisibility() == VISIBLE) {
            if (curShape != null) {
                float width = curShape.rectF.right - curShape.rectF.left;
                float height = curShape.rectF.bottom - curShape.rectF.top;
                textView.setText("LEFT: " + curShape.rectF.left + " TOP: " + curShape.rectF.top + " WIDTH: "
                        + width + " HEIGHT: " + height);
            } else {
                textView.setText("X: " + downX + " Y: " +downY);
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
                curShape.setRectF();
            }

        }

        invalidate();

        TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.position_textview);
        if (textView.getVisibility() == VISIBLE) {
            if (curShape != null) {
                float width = curShape.rectF.right - curShape.rectF.left;
                float height = curShape.rectF.bottom - curShape.rectF.top;
                textView.setText("LEFT: " + curShape.rectF.left + " TOP: " + curShape.rectF.top + " WIDTH: "
                        + width + " HEIGHT: " + height);
            } else {
                textView.setText("X: " + downX + " Y: " +downY);
            }
        }
    }
    public void upEventHandler(MotionEvent event) {
        if (isClick && curShape != null) {
            //popupWindowMain.showAsDropDown(((Activity) getContext()).findViewById(R.id.insert_shape));
            popupWindowMain.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden),0,0);
        }
        if (curShape != null) {
            gameDatabase.updateShape(curShape);
        }
        invalidate();
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

    public void initPopupWindowPage() {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_page, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowPage = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindowPage.setContentView(popupView);
        popupWindowPage.setTouchable(true);
        popupWindowPage.setOutsideTouchable(true);
        popupWindowPage.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindowPage.setAnimationStyle(R.style.AnimationFade);
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

        hidden = (Switch) popupView.findViewById(R.id.hidden_switch);
        movable = (Switch) popupView.findViewById(R.id.movable_switch);
        resizeSeekbar = (SeekBar) popupView.findViewById(R.id.resize_seekbar);


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

    public void initPopupWindowSettings() {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_settings, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowSettings = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindowSettings.setContentView(popupView);
        popupWindowSettings.setTouchable(true);
        popupWindowSettings.setOutsideTouchable(true);
        popupWindowSettings.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindowSettings.setAnimationStyle(R.style.AnimationFade);
        position = (Switch) popupView.findViewById(R.id.position_switch);
        pageSwitch = (Switch) popupView.findViewById(R.id.page_switch);
        uniquePageSwitch = (Switch) popupView.findViewById(R.id.unique_page_switch);

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
        hidden.setChecked(getCurShape().hidden);
        movable.setChecked(getCurShape().movable);



        hidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    curShape.setHidden(true);
                } else {
                    curShape.setHidden(false);
                }
                gameDatabase.updateShape(curShape);
            }
        });

        movable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    curShape.setMovable(true);
                } else {
                    curShape.setMovable(false);
                }
                gameDatabase.updateShape(curShape);
            }
        });
        resizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curShape.setSize(progress / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
//        Toast toast = Toast.makeText(getContext(), curShape.getScript(), Toast.LENGTH_SHORT);
//        toast.show();
//        System.out.println(curShape.getRawScript());

        final EditView editView = (EditView) findViewById(R.id.editView);
        String curScript = editView.getCurShape().getScript();
        final EditText editText = new EditText(getContext());
        editText.setText(curScript);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("View or Change Script");
        builder.setView(editText);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String changedScript = editText.getText().toString();
                if (!changedScript.isEmpty()) {
                    editView.getCurShape().setScript(changedScript);
                    editView.gameDatabase.updateShape(editView.getCurShape());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


    }

    public void flushTmpScriptToRawScript() {
        for (String s : tmpScript) {
            if (s.isEmpty()) continue;
            getCurShape().script += s + " ";
        }
        //getCurShape().script = getCurShape().rawScript;
        gameDatabase.updateShape(curShape);
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

    public void showSettings() {
        int width = popupWindowSettings.getContentView().getMeasuredWidth();
        popupWindowSettings.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden), 0, 0);
        position.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.position_textview);
                    textView.setVisibility(VISIBLE);
                } else {
                    TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.position_textview);
                    textView.setVisibility(INVISIBLE);
                }
            }
        });
        pageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.page_textview);
                    textView.setVisibility(VISIBLE);
                    textView.setText(curGameName+curPageName);
                } else {
                    TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.page_textview);
                    textView.setVisibility(INVISIBLE);
                }
            }
        });
        uniquePageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.unique_page_textview);
                    textView.setVisibility(VISIBLE);
                    textView.setText(pageUniqueList.get(curPageIndex));
                } else {
                    TextView textView = (TextView) ((Activity)getContext()).findViewById(R.id.unique_page_textview);
                    textView.setVisibility(INVISIBLE);
                }
            }
        });
    }
}
