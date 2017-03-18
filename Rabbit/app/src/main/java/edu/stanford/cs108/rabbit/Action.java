package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricX on 11/3/17.
 */

public abstract class Action {

    List<String> actionList;
    MediaPlayer mp;
    GameView gameView;
    InventoryView inventoryView;
    List<Shape> inventoryShapeList;
    Context context;
    GameDatabase gameDatabase;
    Page currPage;
    List<Shape> currShapeList;
    MediaPlayer mediaPlayer;

    public Action(List<String> actionList) {
        this.actionList = actionList;
        gameView = Shape.gameView;
        inventoryView = Shape.inventoryView;
        context = Shape.context;
        gameDatabase = GameDatabase.getInstance();
        gameDatabase.getDb(context);
    }

    public void setPage() {
        currPage = gameView.getCurrPage();
        currShapeList = new ArrayList<>();
        for (Shape shape : currPage.getShapeList()) currShapeList.add(shape);
        for (Shape shape : inventoryView.getInventoryShapes()) currShapeList.add(shape);
    }

    //Actions handlers: onGoto, onShow, onHide, onPlay.
    public void onGoto(String pageName) {
        setPage();
        Page newPage = gameDatabase.getPage(pageName.trim());  //Implemented a dummy selectPage() in GameDatabase, should delete that.
        gameView.setCurrentPage(newPage);
        String prevPageName = currPage.getUniqueName();
    }


    public void onShow(String shapeName) {
        setPage();
        Shape targetShape = null;
        for (int i = 0; i<currShapeList.size(); i++) {
            if (currShapeList.get(i).getUniqueName().equals(shapeName)) {
                targetShape = currShapeList.get(i);
                break;
            }
        }
        if (targetShape == null) {
            targetShape = gameDatabase.getShape(shapeName);
        }
        targetShape.setHidden(false);
        //gameDatabase.updateShape(targetShape);
        gameView.invalidate();
        //need to see if this shape "shapeName" is in the current page, if so, invalidate();
    }

    public void onHide(String shapeName) {
        Shape targetShape = null;
        setPage();
        for (int i = 0; i<currShapeList.size(); i++) {
            if (currShapeList.get(i).getUniqueName().equals(shapeName)) {
                targetShape = currShapeList.get(i);
                targetShape.setHidden(true);
            }
        }
        if (targetShape == null) {
            targetShape = gameDatabase.getShape(shapeName);
        }
        targetShape.setHidden(true);
        //gameDatabase.updateShape(targetShape);
        gameView.invalidate();
        //need to see if this shape "shapeName" is in the current page, if so, invalidate();

    }

    public void onPlay(String soundName) {
        int soundId = context.getResources().getIdentifier(soundName, Shape.RAW, context.getPackageName());
        mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.start();
    }

}
