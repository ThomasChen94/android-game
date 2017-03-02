package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;

import org.json.JSONObject;

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
                    + "name TEXT, page TEXT, image TEXT, sound TEXT, text TEXT, script TEXT,"
                    + "left FLOAT, top FLOAT, right FLOAT, bottom FLOAT, hidden INTEGER, movable INTEGER,"
                    + " _id INTEGER PRIMARY KEY AUTOINCREMENT);";

            //System.out.println(setupStr);
            db.execSQL(setupStr);

        }

        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
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
