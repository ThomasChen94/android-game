package edu.stanford.cs108.rabbit;

<<<<<<< Updated upstream
import android.content.Context;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.View;
=======
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.content.Context;
>>>>>>> Stashed changes
import org.json.JSONObject;

/**
 * Created by qianyu on 2017/2/26.
 */

public abstract class Shape {
    // fields
<<<<<<< Updated upstream
    protected String id;
    protected int page;
    protected RectF rectF;
    protected JSONObject script;
    protected String image;
    protected String text;
    protected boolean hidden;
    protected boolean movable;
    protected String soundName;

    protected static final String PACKAGE_NAME = "edu.stanford.cs108.rabbit";
    protected static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";

    protected MediaPlayer mp;



=======
    String id;
    int page;
    RectF rectF;
    JSONObject script;
    String image;
    String text;
    boolean hidden;
    boolean movable;
    String soundName;



    static int currentShapeNumber = 0; // counting the current shapes in our app

    final String[] scriptPrimitives = new String[] {"goto", "play", "hide", "show"};
    final String[] scriptTriggers = new String[] {"onClick", "onEnter", "onDrop"};

    static final String PACKAGE_NAME = "edu.stanford.cs108.rabbit";
    static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";
>>>>>>> Stashed changes

    protected MediaPlayer mp;

    // methods
<<<<<<< Updated upstream
    public Shape() {
        this.soundName = "evillaugh";
        this.rectF = new RectF(0,0,0,0);
    }

    public Shape(String id, int page, JSONObject script, String image, String text, boolean hidden, boolean movable, String soundName) {
        this();
        this.id = id;
        this.page = page;
        this.script = script;
        this.image = image;
        this.text = text;
        this.hidden = hidden;
        this.movable = movable;
        this.soundName = soundName;
=======
    public Shape(String image, String text, String soundName) {
        currentShapeNumber++; // every time constructing a new shape, increment the counter

        this.image = image;
        this.text = text;
        this.soundName = soundName;

        hidden = false;
        movable = false;

        script = new JSONObject();
    }

    public void draw(Canvas canvas) {

    }

    public void playSound(Context context) {
        int soundId = context.getResources().getIdentifier(soundName, RAW, context.getPackageName());
        mp = MediaPlayer.create(context, soundId);
        mp.start();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
>>>>>>> Stashed changes
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(float left, float top, float right, float bottom) {
        this.rectF = new RectF(left, top, right, bottom);
    }

    public JSONObject getScript() {
        return script;
    }

    public void setScript(JSONObject script) {
        this.script = script;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

<<<<<<< Updated upstream
    public void playSound(Context context) {
        int soundId = context.getResources().getIdentifier(soundName, RAW, context.getPackageName());
        mp = MediaPlayer.create(context, soundId);
        mp.start();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(float left, float top, float right, float bottom) {
        this.rectF = new RectF(left, top, right, bottom);
    }

    public JSONObject getScript() {
        return script;
    }

    public void setScript(JSONObject script) {
        this.script = script;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

=======
>>>>>>> Stashed changes
    public void setText(String text) {
        this.text = text;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }
}