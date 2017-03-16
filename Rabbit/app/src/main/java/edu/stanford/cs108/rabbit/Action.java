package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.List;

/**
 * Created by EricX on 11/3/17.
 */

public abstract class Action {

    List<String> actionList;
    MediaPlayer mp;
    GameView gameView;
    Context context;
    GameDatabase gameDatabase;
    Page currPage;
    List<Shape> currShapeList;
    MediaPlayer mediaPlayer;

    public Action(List<String> actionList) {
        this.actionList = actionList;
        gameView = Shape.gameView;
        context = Shape.context;
        gameDatabase = GameDatabase.getInstance();
        gameDatabase.getDb(context);
    }

    public void setPage() {
        currPage = gameView.getCurrPage();
        currShapeList = currPage.getShapeList();
    }

    //Actions handlers: onGoto, onShow, onHide, onPlay.
    public void onGoto(String pageName) {
        setPage();
        Page newPage = gameDatabase.getPage(pageName.trim());  //Implemented a dummy selectPage() in GameDatabase, should delete that.
        gameView.setCurrentPage(newPage);
        String prevPageName = currPage.getUniqueName();
        //gameView.invalidate();
//        if (!newPage.equals(prevPageName)) {
//            setPage();
//            processOnEnter();
//        }
    }

//    private void processOnEnter() {
//
//    }

    public void onShow(String shapeName) {
        System.out.println("shapeName is: " + shapeName );
        setPage();
        Shape targetShape = null;
        for (int i = 0; i<currShapeList.size(); i++) {
            System.out.println("current shape name is: " + currShapeList.get(i).getUniqueName() );
            if (currShapeList.get(i).getUniqueName().equals(shapeName)) {
                targetShape = currShapeList.get(i);
                System.out.println("targetShape found" );
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
            System.out.println("currShape uniquename: " + currShapeList.get(i).getUniqueName());
            System.out.println("looking for uniquename: " + shapeName);
            if (currShapeList.get(i).getUniqueName().equals(shapeName)) {
                targetShape = currShapeList.get(i);
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
