package edu.stanford.cs108.rabbit;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;


public class GameActivity extends Activity {
    GameView gameView;
    ImageView bagView;
    WrappingSlidingDrawer wrappingSlidingDrawer;
    View handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);
        //Shape.setContext(getApplicationContext());   setContext has been moved to GameView's constructor
        gameView = (GameView) findViewById(R.id.gameView);
        inventoryView = (InventoryView) findViewById(R.id.inventory);
        Shape.setGameView(gameView);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getInventoryDimensions();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downEventHandler(event);
                System.out.println("Action_down!!!!");
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

    Page currPage;
    List<Shape> pageShapeList;
    Shape selectedShape;
    float relativeX;
    float relativeY;
    boolean isClick;

    InventoryView inventoryView;
    List<Shape> inventoryShapeList;

    private void downEventHandler(MotionEvent event) {
        //testInventoryDimension();

        isClick = true;
        inventoryIsOn = wrappingSlidingDrawer.isOpened();
        selectedShape = null;
        float downX = event.getX();
        float downY = event.getY();
        String curPos = "x: " + downX + " y: " +downY;
        //System.out.println(curPos);
        Toast toast = Toast.makeText(getApplicationContext(), curPos, Toast.LENGTH_SHORT);
        toast.show();
        if (!inventoryIsOn || !isClickOnInventory((int)downX, (int)downY) ) {//Not click on inventory); test if clicked on some shape
            currPage = gameView.getCurrPage();
            pageShapeList = currPage.getShapeList();
            findShape(pageShapeList, downX, downY); //assign shape found to selectedShape;
//            if (selectedShape != null) {
//                relativeX = downX - selectedShape.getRectF().left;
//                relativeY = downY - selectedShape.getRectF().top;
//            }
        } else { //TODO: clicked in inventory area
            inventoryShapeList = inventoryView.getInventoryShapes();
            findShape(inventoryShapeList, downX, downY);

        } //|| !isClickOnInventory((int)downX, (int)downY)

        if (selectedShape != null) {
            System.out.println("selectedSHape is not null" ); //+ selectedShape == null
            relativeX = downX - selectedShape.getRectF().left;
            relativeY = downY - selectedShape.getRectF().top;
        }

    }

    private void findShape(List<Shape> shapeList, float downX, float downY) {
        for (int i = shapeList.size()-1; i>=0; i--) {
            if (shapeList.get(i).getRectF().contains(downX, downY)) {
                selectedShape =  shapeList.get(i);
                System.out.println(selectedShape.getName());
                //uncomment this to allow selection to appear on top of all. Problematic when selection is big and not movable ,
                // so selection will cover and hide other shape
//                shapeList.remove(i);
//                shapeList.add(selectedShape);
//                gameView.invalidate();
                return;
            }
        }
        selectedShape = null;
    }



    private void dragEventHandler(MotionEvent event) {
        isClick = false;
        float downX = event.getX();
        float downY = event.getY();
        if (selectedShape != null ) { //&& selectedShape.movable == true
            float rectX = downX - relativeX;
            float rectY = downY - relativeY;
            float gameViewWidth = gameView.getViewWidth();
            float gameViewHeight = gameView.getViewHeight();
            rectX = Math.min(gameViewWidth - selectedShape.getRectF().width(), Math.max(0, rectX));
            rectY = Math.min(gameViewHeight - selectedShape.getRectF().height(), Math.max(0, rectY));
            selectedShape.setRectFLeftTop(rectX, rectY);
        }
        gameView.invalidate();
        inventoryView.invalidate();
    }

    private void upEventHandler(MotionEvent event) {
        if (selectedShape != null) {
            if (isClick) {
                //selectedShape.onClick();
            } else {
                //selectedShape.onDrop();
            }
        }

    }

    Rect bagRect;
    Rect inventoryRect;
    Rect wrappingSlidingDrawerRect;
    Rect turnOnInventoryRect;
    Rect turnOffInventoryRect;
    boolean inventoryIsOn;

    private void getInventoryDimensions() {
        bagView = (ImageView) findViewById(R.id.bag);
        bagRect = new Rect();
        bagView.getGlobalVisibleRect(bagRect);

        inventoryView = (InventoryView) findViewById(R.id.inventory);//初始化略过不表
        inventoryRect = new Rect();
        inventoryView.getGlobalVisibleRect(inventoryRect);//此时rec的坐标就是以屏幕左上角为基础的.

        wrappingSlidingDrawer = (WrappingSlidingDrawer) findViewById(R.id.wrapping_sliding_drawer);
        wrappingSlidingDrawerRect = new Rect();
        wrappingSlidingDrawer.getGlobalVisibleRect(wrappingSlidingDrawerRect);

        //wrappingSlidingDrawerRect is the whole area of inventory when inventory is on.
        turnOnInventoryRect = new Rect(bagRect.left, bagRect.top, wrappingSlidingDrawerRect.right, wrappingSlidingDrawerRect.bottom);
        turnOffInventoryRect = new Rect(bagRect.left,wrappingSlidingDrawerRect.top, wrappingSlidingDrawerRect.right, inventoryRect.top);

    }

    private boolean isClickOnInventory(int x, int y) {
        return inventoryRect.contains(x, y);
    }

    private void testInventoryDimension() {
        wrappingSlidingDrawer = (WrappingSlidingDrawer) findViewById(R.id.wrapping_sliding_drawer);
        wrappingSlidingDrawerRect = new Rect();
        wrappingSlidingDrawer.getGlobalVisibleRect(wrappingSlidingDrawerRect);
        Rect rect = wrappingSlidingDrawerRect;
        System.out.println("WrappingDrawer: " + "left: " + rect.left + ", right: " + rect.right + ", top: " + rect.top + ", bottom: " + rect.bottom);

        inventoryView = (InventoryView) findViewById(R.id.inventory);//初始化略过不表
        inventoryRect = new Rect();
        inventoryView.getGlobalVisibleRect(inventoryRect);//此时rec的坐标就是以屏幕左上角为基础的.
        rect = inventoryRect;
        System.out.println("Inventory: " + "left: " + rect.left + ", right: " + rect.right + ", top: " + rect.top + ", bottom: " + rect.bottom);

        bagView = (ImageView) findViewById(R.id.bag);
        bagRect = new Rect();
        bagView.getGlobalVisibleRect(bagRect);

        rect = bagRect;

        System.out.println("Bag: "+"left: " + rect.left + ", right: " + rect.right + ", top: " + rect.top + ", bottom: " + rect.bottom);

        rect = turnOnInventoryRect;
        System.out.println("Turn on: "+"left: " + rect.left + ", right: " + rect.right + ", top: " + rect.top + ", bottom: " + rect.bottom);

        rect = turnOffInventoryRect;
        System.out.println("Turn off: "+"left: " + rect.left + ", right: " + rect.right + ", top: " + rect.top + ", bottom: " + rect.bottom);


    }
}
