package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.content.Context;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.Iterator;


/**
 * Created by qianyu on 2017/2/26.
 */

public abstract class Shape {
    // fields

    String id;
    int page;
    RectF rectF;
    JSONObject script;
    JSONObject onClickJSON;
    JSONObject onDropJSON;
    JSONObject onEnterJSON;

    String image;
    String text;
    boolean hidden;
    boolean movable;
    String soundName;

    Activity activity;
    DisplayView displayView;



    static int currentShapeNumber = 0; // counting the current shapes in our app

    final String[] scriptPrimitives = new String[] {"goto", "play", "hide", "show"};
    final String[] scriptTriggers = new String[] {"onClick", "onEnter", "onDrop"};

    static final String PACKAGE_NAME = "edu.stanford.cs108.rabbit";
    static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";


    protected MediaPlayer mp;

    // methods

    public Shape() {
        this.soundName = "evillaugh";
        this.rectF = new RectF(0,0,0,0);

    }




    private String testScript = "{"
            + "\"on click\": {\"goto\": \"page1\", \"show\": \"carrot\"},"
            + "\"on enter\": {\"show\": \"carrot\"},"
            + "\"on drop\": {\"show\": \"diamond\"}"
            + "}";

    private String testScript2 = "{\"on click\": {\"goto\": \"page1\", \"show\": \"carrot\"}}";



    private void scriptParser() throws JSONException {
        JSONObject jsonObject = new JSONObject(testScript2);

        onClickJSON = jsonObject.getJSONObject("on click");
        onEnterJSON = jsonObject.getJSONObject("on enter");
        onDropJSON = jsonObject.getJSONObject("on drop");
    }

    public void onClick() throws JSONException {
        actionProcesser(onClickJSON);
    }

    public void onDrop() throws JSONException {
        actionProcesser(onDropJSON);
    }

    public void onEnter() throws JSONException {
        actionProcesser(onEnterJSON);
    }

    private void actionProcesser(JSONObject jsonObject) throws JSONException {
        Iterator<String> keys = jsonObject.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            String value = jsonObject.getString(key);
            switch (key) {
                case "goto": onGoto(value);
                    break;
                case "show": onShow(value);
                    break;
                case "hide": onHide(value);
                    break;
                case "play": onPlay(value);
            }
        }
    }

    public void onGoto(String pageName) {
        GameDatabase gameDatabase = GameDatabase.getInstance();
        Page newPage = gameDatabase.selectPage(pageName);  //Implemented a dummy selectPage() in GameDatabase, should delete that.
        displayView.setCurrentPage(newPage);


    }

    //TODO: waiting for db's update shape method
    public void onShow(String shapeName) {
        //need to see if this shape "shapeName" is in the current page, if so, invalidate();
    }

    //TODO: waiting for db's update shape method
    public void onHide(String shapeName) {
        //need to see if this shape "shapeName" is in the current page, if so, invalidate();

    }

    public void onPlay(String soundName) {
        int soundId = activity.getResources().getIdentifier(soundName, RAW, activity.getPackageName());
        mp = MediaPlayer.create(activity, soundId);
        mp.start();
    }

    public Shape(String image, String text, String soundName) {
        currentShapeNumber++; // every time constructing a new shape, increment the counter

        this.image = image;
        this.text = text;
        this.soundName = soundName;

        hidden = false;
        movable = false;

        script = new JSONObject();
        try {                       //parsing script into individual Json objects for 3 action triggers
            scriptParser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    BitmapDrawable imageDrawable;
    private void initBitmapDrawable() {
        if (image == null || image.equals("")) {
            imageDrawable = null;
        } else {
            int imageId = activity.getResources().getIdentifier(image, RAW, activity.getPackageName());
            imageDrawable =
                    (BitmapDrawable) activity.getResources().getDrawable(imageId);
        }
    }

    //TODO: need to organize different versions of constructor
    public Shape(Activity activity) {  //we need to have a constructor that takes an activity for using findViewById()
        this.activity = activity;
        displayView = (DisplayView) activity.findViewById(R.id.displayView);
        initBitmapDrawable();
    }

    public void draw(Canvas canvas) {

    }

    //TODO: can be merged with onPlay(); don't need to pass a Context, instead we can use Activity.
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
