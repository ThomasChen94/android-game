package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qianyu on 2017/2/26.
 */

public final class GameDatabase {
    private SQLiteDatabase db;
    private static GameDatabase ourInstance = new GameDatabase();
    public static int count;

    private GameDatabase() {
        db = null;
        count = 0;
    }

    public SQLiteDatabase getDb(Context context) {
        if (db == null) {
            db = context.openOrCreateDatabase("ShapesDB", context.MODE_PRIVATE, null);
        }
        Cursor tablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='shapes';", null);
        if (tablesCursor.getCount() == 0) {
            String setupStr = "CREATE TABLE shapes ("
                    + "name TEXT, page Text, image TEXT, sound TEXT, text TEXT, script TEXT,"
                    + "left FLOAT, top FLOAT, right FLOAT, bottom FLOAT, hidden BOOLEAN, movable BOOLEAN,"
                    + " _id INTEGER PRIMARY KEY AUTOINCREMENT);";

            //System.out.println(setupStr);
            db.execSQL(setupStr);
        }
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public Page getPage(int pageIndex) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM shapes WHERE page = " + pageIndex + "and name not like %info;", null);
        if(cursor.moveToFirst() == false) return new Page("", "", null); // if nothing is selected, return an empty page
        List<Shape> shapeList= new LinkedList<Shape>();
        while(cursor.moveToNext()) {
            String sound    = cursor.getString(cursor.getColumnIndex("sound"));
            String image    = cursor.getString(cursor.getColumnIndex("image"));
            String text     = cursor.getString(cursor.getColumnIndex("text"));
            String script   = cursor.getString(cursor.getColumnIndex("script"));
            boolean hidden  = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("hidden")));
            boolean movable = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("movable")));
            int order    = cursor.getShort(cursor.getColumnIndex("order"));
            int left     = cursor.getShort(cursor.getColumnIndex("left"));
            int top      = cursor.getShort(cursor.getColumnIndex("top"));
            int right    = cursor.getShort(cursor.getColumnIndex("right"));
            int bottom   = cursor.getShort(cursor.getColumnIndex("bottom"));
            int type     = cursor.getShort(cursor.getColumnIndex("type"));

            Shape newShape = null;
            if(type == 0) {
                newShape = new TextShape(image, text, sound, script, order, hidden, movable, left, top, right, bottom);
            } else {
                newShape = new ImageShape(image, text, sound, script, order, hidden, movable, left, top, right, bottom);
            }
            shapeList.add(newShape);
        }

        Cursor pageCursor = db.rawQuery(
                "SELECT * FROM shapes WHERE page = " + pageIndex + "and name like %info;", null);
        String sound    = pageCursor.getString(pageCursor.getColumnIndex("sound"));
        String image    = pageCursor.getString(pageCursor.getColumnIndex("image"));
        return new Page(image, sound, shapeList);
    }

    public static GameDatabase getInstance() {
        return ourInstance;
    }

    public void addShape(Shape shape) {
        if (db == null) return;
        String dataStr = "INSERT INTO shapes VALUES ("
                + "\"" + shape.getId() + "\","
                + "\"" + shape.getPage() + "\","
                + "\"" + shape.getImage() + "\","
                + "\"" + shape.getSoundName() + "\","
                + "\"" + shape.getText() + "\","
                + "\"" + shape.getScript() + "\","
                + shape.getRectF().left + ","
                + shape.getRectF().top + ","
                + shape.getRectF().right + ","
                + shape.getRectF().bottom + ","
                + (shape.isHidden() ? 1 : 0) + ","
                + (shape.isMovable() ? 1 : 0) + ","
                + "NULL);";
        //System.out.println(dataStr);
        db.execSQL(dataStr);

        count++;
    }

    public void deleteShape(Shape shape) {
        if (db == null) return;
        String queryStr = "DELETE FROM shapes WHERE name = "
                + "\"" + shape.getId() + "\";";
        db.execSQL(queryStr);

        count--;
    }
}
