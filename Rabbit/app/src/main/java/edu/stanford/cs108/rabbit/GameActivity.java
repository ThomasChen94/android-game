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
    GameDatabase gameDatabase;

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

        gameDatabase = GameDatabase.getInstance();
        gameDatabase.getDb(Shape.context);

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
    boolean selectInventoryItem = false;
    int inventoryHeightOffset;

    InventoryView inventoryView;
    List<Shape> inventoryShapeList;

    private void downEventHandler(MotionEvent event) {


        isClick = true;
        selectInventoryItem = false;
        inventoryIsOn = wrappingSlidingDrawer.isOpened();
        selectedShape = null;
        currPage = gameView.getCurrPage();
        pageShapeList = currPage.getShapeList();
        inventoryShapeList = inventoryView.getInventoryShapes();
        inventoryHeightOffset = inventoryRect.top;
        isDragToInventory = false;
        isDragToPage = false;



        float downX = event.getX();
        float downY = event.getY();
        String curPos = "x: " + downX + " y: " +downY;
        Toast toast = Toast.makeText(getApplicationContext(), curPos, Toast.LENGTH_SHORT);
        toast.show();
        if (!inventoryIsOn || !isClickOnInventory((int)downX, (int)downY) ) {//Not click on inventory); test if clicked on some shape
            findShape(pageShapeList, downX, downY); //assign shape found to selectedShape;
        } else { //TODO: clicked in inventory area
            selectInventoryItem = true;
            downY = downY - (gameView.getViewHeight() - inventoryView.getViewHeight());
            findShape(inventoryShapeList, downX, downY);
        } //|| !isClickOnInventory((int)downX, (int)downY)

        if (selectedShape != null) {
            relativeX = downX - selectedShape.getRectF().left;
            relativeY = downY - selectedShape.getRectF().top;
        }

        testInventoryDimension();
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


    boolean isDragToInventory = false;
    boolean isDragToPage = false;
    Shape selectedShapeCopy = null;

    private void dragEventHandler(MotionEvent event) {
        isClick = false;
        if (selectedShape != null && selectedShape.movable == true) { //Selected a shape
            if (!selectInventoryItem) { //Selected a shape in page
                moveShapeInPage(event);
//                if (inventoryIsOn && enterInventory()) { //moving the shape from page to inventory
//                    isDragToInventory = true;
//                    moveShapeToInventory();
//                } else {
//                    isDragToInventory = false;
//                    if (inventoryShapeList.contains(selectedShapeCopy)) inventoryShapeList.remove(selectedShapeCopy);
//
//                }
                if (inventoryIsOn) moveShapeToInventory();


            }
            else { //Selected a shape in inventory
                moveShapeInInventory(event);
                moveShapeToPage();
            }
        }
        gameView.invalidate();
        inventoryView.invalidate();
    }

    private void moveShapeToInventory() {
        isDragToInventory = true;
        if (selectedShapeCopy == null) selectedShapeCopy = Shape.deepCopyShape(selectedShape);
        selectedShapeCopy.setRectFLeftTop(selectedShape.getRectF().left, selectedShape.getRectF().top - inventoryHeightOffset);
        if (!inventoryShapeList.contains(selectedShapeCopy)) {
//            System.out.println("inventoryView.getTop()" + inventoryView.getTop());
//            System.out.println("adding: left = " + selectedShapeCopy.getRectF().left + ", top = " + selectedShapeCopy.getRectF().top);
            inventoryShapeList.add(selectedShapeCopy);
        }
    }

    private void moveShapeToPage() {
        isDragToPage = true;
        if (selectedShapeCopy == null) selectedShapeCopy = Shape.deepCopyShape(selectedShape);
        selectedShapeCopy.setRectFLeftTop(selectedShape.getRectF().left, selectedShape.getRectF().top + inventoryHeightOffset);
        if (!pageShapeList.contains(selectedShapeCopy)) {
            pageShapeList.add(selectedShapeCopy);
        }
    }

    private boolean enterInventory() {
        return isInInventory((int)selectedShape.getRectF().left, (int)selectedShape.getRectF().bottom);
    }

    private boolean isInInventory(int x, int y) {
        return y >= inventoryRect.top;
        //return inventoryRect.contains(x, y);
    }


    private void moveShapeInPage(MotionEvent event) {
        float downX = event.getX();
        float downY = event.getY();
        float rectX = downX - relativeX;
        float rectY = downY - relativeY;
        float gameViewWidth = gameView.getViewWidth();
        float gameViewHeight = gameView.getViewHeight();
        rectX = Math.min(gameViewWidth - selectedShape.getRectF().width(), Math.max(0, rectX));
        rectY = Math.min(gameViewHeight - selectedShape.getRectF().height(), Math.max(0, rectY));
        selectedShape.setRectFLeftTop(rectX, rectY);
    }

    private void moveShapeInInventory(MotionEvent event) {
        float downX = event.getX();
        float downY = event.getY() - (gameView.getViewHeight() - inventoryView.getViewHeight());
        float rectX = downX - relativeX;
        float rectY = downY - relativeY;
        float inventoryViewWidth = inventoryView.getViewWidth();
        float inventoryViewHeight = inventoryView.getViewHeight();
        rectX = Math.min(inventoryViewWidth - selectedShape.getRectF().width(), Math.max(0, rectX));
        rectY = Math.min(inventoryViewHeight - selectedShape.getRectF().height(), rectY);
        selectedShape.setRectFLeftTop(rectX, rectY);
    }

    private void upEventHandler(MotionEvent event) {
        if (selectedShape == null) return;
        float downX = event.getX();
        float downY = event.getY();

        if (isClick) {
            //selectedShape.onClick();
        } else {
            //selectedShape.onDrop();
        }

        if (isDragToInventory) {
            if (isInInventory((int)downX, (int)downY)) { //If ACTION_UP happens inside inventory, add the shape to inventory and remove it from page
                dragToInventory();
            } else {
                inventoryShapeList.remove(selectedShapeCopy);
                if (selectedShape.getRectF().bottom > inventoryRect.top)
                    selectedShape.setRectFLeftTop(selectedShape.getRectF().left, inventoryRect.top - selectedShape.getRectF().height());
            }
        }
        System.out.println("selectedCopy exist?" + inventoryShapeList.contains(selectedShapeCopy));
        if (isDragToPage) {
            if (isInPage((int)downX, (int)downY)) {
                dragToPage();
            } else {
                pageShapeList.remove(selectedShapeCopy);
                selectedShape.setRectFLeftTop(selectedShape.getRectF().left, inventoryRect.top - inventoryHeightOffset); //Second arg is essentially 0 (local inventory y coordinate)
            }
        }

        selectedShapeCopy = null;
        selectedShape = null;
        gameView.invalidate();
        inventoryView.invalidate();
    }

    private boolean isInPage(int downX, int downY) {
        return downY <= inventoryRect.top;
    }

    private void dragToPage() {
        inventoryShapeList.remove(selectedShape);
        //gameDatabase.deleteShape(selectedShape);
        selectedShapeCopy.setPage(currPage.getName());
        if (selectedShapeCopy.getRectF().bottom >= inventoryRect.top)
            selectedShapeCopy.setRectFLeftTop(selectedShapeCopy.getRectF().left, inventoryRect.top - selectedShapeCopy.getRectF().height());

        //gameDatabase.addShape(selectedShapeCopy);
    }

    private void dragToInventory() {
//        if (selectedShape == null) System.out.println("selectedShape == null?" + true);
//        else System.out.println("selectedShape == null?" + false);
        System.out.println("selectedShape is contained?" + pageShapeList.contains(selectedShape));
        pageShapeList.remove(selectedShape);
        //gameDatabase.deleteShape(selectedShape);  //Don't include database for the moment, otherwise my local database will be modified and testing cannot be done.
        selectedShapeCopy.setPage("Inventory");
        selectedShapeCopy.setRectFLeftTop(selectedShapeCopy.getRectF().left, inventoryRect.top - inventoryHeightOffset); //Second arg is essentially 0 (local inventory y coordinate)

        //gameDatabase.addShape(selectedShapeCopy);

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
        System.out.println("Inventory is On?" + inventoryIsOn);
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

        inventoryView.getLocalVisibleRect(inventoryRect);//此时rec的坐标就是以屏幕左上角为基础的.
        rect = inventoryRect;
        System.out.println("Inventory local: " + "left: " + rect.left + ", right: " + rect.right + ", top: " + rect.top + ", bottom: " + rect.bottom);

    }
}
