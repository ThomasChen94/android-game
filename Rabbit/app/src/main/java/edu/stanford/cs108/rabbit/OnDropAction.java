package edu.stanford.cs108.rabbit;

import android.media.MediaPlayer;

import java.util.List;

/**
 * Created by EricX on 11/3/17.
 */

public class OnDropAction extends Action {
    String droppingShapeUniqueName;

    public OnDropAction(List<String> actionList, String droppingShapeUniqueName) {
        super(actionList);
        this.droppingShapeUniqueName = droppingShapeUniqueName;
    }

    public boolean existDropShapeMatch(Shape droppingShape) {
        return droppingShape.getUniqueName().equals(droppingShapeUniqueName);
    }
}
