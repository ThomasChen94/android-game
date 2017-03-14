package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;

import org.json.JSONObject;

import java.util.ArrayList;
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
        Cursor shapeTablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='shapes';", null);
        if (shapeTablesCursor.getCount() == 0) {
            String setupStr = "CREATE TABLE shapes ("
                    + "uniquename TEXT, name TEXT, page Text, image TEXT, sound TEXT, text TEXT, fontsize INTEGER, script TEXT,"
                    + "left FLOAT, top FLOAT, right FLOAT, bottom FLOAT, hidden BOOLEAN, movable BOOLEAN, myorder INTEGER"
                    + ");";

            //System.out.println(setupStr);
            db.execSQL(setupStr);
        }

        Cursor pageTableCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='pages';", null);
        if (pageTableCursor.getCount() == 0) {
            String setupStr = "CREATE TABLE pages ("
                    + "uniquename TEXT, name TEXT, game Text, image TEXT, sound TEXT"
                    + ");";

            //System.out.println(setupStr);
            db.execSQL(setupStr);
        }

        Cursor gameTablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='games';", null);
        if (gameTablesCursor.getCount() == 0) {
            String setupStr = "CREATE TABLE games ("
                    + "uniquename TEXT, name TEXT"
                    + ");";

            //System.out.println(setupStr);
            db.execSQL(setupStr);
        }
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public Page getPage(String page) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM shapes WHERE page = \"" + page + "\" and name not like '%info';", null);
        if(cursor.moveToFirst() == false) return new Page("", "", null, "", "", ""); // if nothing is selected, return an empty page
        List<Shape> shapeList= new LinkedList<Shape>();
        do {
            String sound    = cursor.getString(cursor.getColumnIndex("sound"));
            String image    = cursor.getString(cursor.getColumnIndex("image"));
            String text     = cursor.getString(cursor.getColumnIndex("text"));
            String uniqueName    = cursor.getString(cursor.getColumnIndex("uniquename"));
            String name    = cursor.getString(cursor.getColumnIndex("name"));
            String script   = cursor.getString(cursor.getColumnIndex("script"));
            boolean hidden  = (cursor.getString(cursor.getColumnIndex("hidden")).equals("0")) ? false:true;
            boolean movable = (cursor.getString(cursor.getColumnIndex("movable")).equals("0")) ? false:true;
            //boolean hidden  = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("hidden")));
            //boolean movable = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("movable")));
            //Eric modified this.
            //int order    = cursor.getShort(cursor.getColumnIndex("myorder"));
            int myorder    = cursor.getShort(cursor.getColumnIndex("myorder"));
            //Eric modified this.

            float left     = cursor.getFloat(cursor.getColumnIndex("left")) * Shape.viewWidth;
            float top      = cursor.getFloat(cursor.getColumnIndex("top")) * Shape.viewHeight;
            float right    = cursor.getFloat(cursor.getColumnIndex("right")) * Shape.viewWidth;
            float bottom   = cursor.getFloat(cursor.getColumnIndex("bottom")) * Shape.viewHeight;

            Shape newShape = null;
            System.out.println(image);

            newShape = new Shape(image, text, sound, uniqueName, name, page, script, myorder, hidden, movable, left, top, right, bottom);
            shapeList.add(newShape);
        } while(cursor.moveToNext());
//
//            Cursor pageCursor = db.rawQuery(
//                "SELECT * FROM shapes WHERE page = \"" + page + "\" and name like '%info';", null);
//        pageCursor.moveToNext();
//        String sound    = pageCursor.getString(pageCursor.getColumnIndex("sound"));
//        String image    = pageCursor.getString(pageCursor.getColumnIndex("image"));
        return new Page("", "", shapeList, "", "", "");
    }

    // updata the given shape
    public void updateShape(Shape shape) {
        //gameName = "shapes";   // hard code the game name

        String updateSQL = "UPDATE " + "shapes" + " SET"
                            + " name = \" " + shape.getName() + "\","
                            + " page = \"" + shape.getPage() + "\","
                            + " image = \"" + shape.getImage() + "\","
                            + " sound = \"" + shape.getSoundName() + "\","
                            + " text = \"" + shape.getText() + "\","
                            + " fontsize = " + shape.getFontsize() + ","
                            + " script = \"" + shape.getScript() + "\","
                            + " left = " + shape.getRectF().left / Shape.viewWidth + ","
                            + " top = " + shape.getRectF().top / Shape.viewHeight + ","
                            + " right = " + shape.getRectF().right / Shape.viewWidth + ","
                            + " bottom = " + shape.getRectF().bottom / Shape.viewHeight + ","
                            + " hidden = " + (shape.isHidden() ? 1 : 0) + ","
                            + " movable = " + (shape.isMovable() ? 1 : 0) + ","
                             + " myorder = " + shape.getOrder() + " "
                            + "WHERE uniquename = \"" + shape.getUniqueName() + "\";";
        db.execSQL(updateSQL);
    }


    public boolean ifExistShape (Shape shape) {
        Cursor shapeCursor = db.rawQuery(
                "SELECT * FROM shapes WHERE uniquename = " + shape.getUniqueName() + ";", null);
        if(shapeCursor.moveToFirst() == false) return false;
        return true;
    }

    public static GameDatabase getInstance() {
        return ourInstance;
    }

    public Shape getShape(String uniquename) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM shapes WHERE uniquename = " + uniquename + ";", null);
        cursor.moveToFirst();
        String sound    = cursor.getString(cursor.getColumnIndex("sound"));
        String image    = cursor.getString(cursor.getColumnIndex("image"));
        String text     = cursor.getString(cursor.getColumnIndex("text"));
        String uniqueName    = cursor.getString(cursor.getColumnIndex("uniquename"));
        String name    = cursor.getString(cursor.getColumnIndex("name"));
        String page    = cursor.getString(cursor.getColumnIndex("page"));
        String script   = cursor.getString(cursor.getColumnIndex("script"));
        boolean hidden  = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("hidden")));
        boolean movable = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("movable")));
        //Eric modified this.
        //int order    = cursor.getShort(cursor.getColumnIndex("myorder"));
        int myorder    = cursor.getShort(cursor.getColumnIndex("myorder"));
        //Eric modified this.

        float left     = cursor.getFloat(cursor.getColumnIndex("left")) * Shape.viewWidth;
        float top      = cursor.getFloat(cursor.getColumnIndex("top")) * Shape.viewHeight;
        float right    = cursor.getFloat(cursor.getColumnIndex("right")) * Shape.viewWidth;
        float bottom   = cursor.getFloat(cursor.getColumnIndex("bottom")) * Shape.viewHeight;


        Shape newShape = new Shape(image, text, sound, uniqueName, name, page, script, myorder, hidden, movable, left, top, right, bottom);
        return newShape;
    }

    public void addShape(Shape shape) {
        if (db == null) return;
        String dataStr = "INSERT INTO shapes VALUES ("
                + "\"" + shape.getUniqueName() + "\","
                + "\"" + shape.getName() + "\","
                + "\"" + shape.getPage() + "\","
                + "\"" + shape.getImage() + "\","
                + "\"" + shape.getSoundName() + "\","
                + "\"" + shape.getText() + "\","
                + "\"" + shape.getFontsize() + "\","
                + "\"" + shape.getScript() + "\","
                + shape.getRectF().left / Shape.viewWidth + ","
                + shape.getRectF().top / Shape.viewHeight + ","
                + shape.getRectF().right / Shape.viewWidth + ","
                + shape.getRectF().bottom / Shape.viewHeight + ","
                + (shape.isHidden() ? 1 : 0) + ","
                + (shape.isMovable() ? 1 : 0) + ","
                + shape.getOrder()
                + ");";
        //System.out.println(dataStr);
        db.execSQL(dataStr);

        count++;
    }

    // add a new page
    public void addPage(Page page) {
        String insertSQL = "INSERT INTO pages VALUES ("
                + "\"" + page.getUniqueName() + "\","
                + "\"" + page.getName() + "\","
                + "\"" + page.getGame() + "\","
                + "\"" + page.getBackground() + "\","
                + "\"" + page.getSoundName() + "\");";
        db.execSQL(insertSQL);
    }

    public void updatePage(Page page) {
        String updateSQL = "UPDATE " + "pages" + " SET"
                + " name = \" " + page.getUniqueName() + "\","
                + " page = \"" + page.getName() + "\","
                + " image = \"" + page.getBackground() + "\","
                + " sound = \"" + page.getSoundName() + "\" "
                + "WHERE uniquename = \"" + page.getUniqueName()  + "\"; ";
        db.execSQL(updateSQL);
    }

    public void addGame(String uniqueName, String name) {
        String insertSQL = "INSERT INTO games VALUES ("
                + "\"" + uniqueName + "\","
                + "\"" + name + "\");";
        db.execSQL(insertSQL);
    }

    public List<Page> getGame(String gameUniquename) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM pages WHERE game = \"" + gameUniquename + "\";", null);
        List<Page> pageList = new ArrayList<Page>();
        while(cursor.moveToNext()) {
            String name    = cursor.getString(cursor.getColumnIndex("name"));
            String uniquename    = cursor.getString(cursor.getColumnIndex("uniquename"));
            String game    = cursor.getString(cursor.getColumnIndex("game"));
            String sound    = cursor.getString(cursor.getColumnIndex("sound"));
            String image    = cursor.getString(cursor.getColumnIndex("image"));
            pageList.add(new Page(image, sound, null, name, uniquename, game));
        }
        return pageList;
    }

    public List<String> getGameNameList() {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM games;", null);
        List<String> gameList = new ArrayList<String>();
        while(cursor.moveToNext()) {
            String name    = cursor.getString(cursor.getColumnIndex("name"));
            gameList.add(name);
        }
        return gameList;
    }

    public void updateGame(String uniqueName, String newName) {
        String updateSQL = "UPDATE " + "games" + " SET"
                + " name = \" " + newName + "\" "
                + "WHERE uniquename = \"" + uniqueName  + "\"; ";
        db.execSQL(updateSQL);
    }

    public boolean renameGame(String prevName, String newName) {
        Cursor cursorPrev = db.rawQuery(
                "SELECT * FROM games where name = \"" + prevName + "\";", null);
        if(cursorPrev.moveToFirst() == false) return false;

        Cursor cursorNew = db.rawQuery(
                "SELECT * FROM games where name = \"" + newName + "\";", null);
        if(cursorNew.moveToFirst()) return false;

        String uniquename    = cursorPrev.getString(cursorPrev.getColumnIndex("uniquename"));
        String updateGameName = "UPDATE " + "games" + " SET"
                + " name = \" " + newName + "\""
                + "WHERE uniquename = \"" + uniquename  + "\"; ";
        db.execSQL(updateGameName);
        return true;
    }

    public boolean renamePage(String uniqueName, String newName) {
        Cursor cursorPrev = db.rawQuery(
                "SELECT * FROM pages where name = \"" + uniqueName + "\";", null);
        if(cursorPrev.moveToFirst() == false) return false;
        String game    = cursorPrev.getString(cursorPrev.getColumnIndex("game"));

        Cursor cursorNew = db.rawQuery(
                "SELECT * FROM pages where name = \"" + newName + "\" and game = \"" + game + "\";", null);
        if(cursorNew.moveToFirst()) return false;
        String updatePageName = "UPDATE " + "pages" + " SET"
                + " name = \" " + newName + "\""
                + "WHERE uniquename = \"" + uniqueName  + "\"; ";
        db.execSQL(updatePageName);
        return true;
    }

    public boolean renameShape(String uniqueName, String newName) {
        Cursor cursorPrev = db.rawQuery(
                "SELECT * FROM shapes where name = \"" + uniqueName + "\";", null);
        if(cursorPrev.moveToFirst() == false) return false;
        String page    = cursorPrev.getString(cursorPrev.getColumnIndex("page"));

        Cursor cursorNew = db.rawQuery(
                "SELECT * FROM shapes where name = \"" + newName + "\"" +
                        "and page IN (" + "SELECT * FROM pages where uniquename = \"" + page + "\"" + ");", null);
        if(cursorNew.moveToFirst()) return false;

        String updateShapeName = "UPDATE " + "shapes" + " SET"
                + " name = \" " + newName + "\""
                + "WHERE uniquename = \"" + uniqueName  + "\"; ";
        db.execSQL(updateShapeName);
        return true;
    }

    public void deleteShape(Shape shape) {
        if (db == null) return;
        String queryStr = "DELETE FROM shapes WHERE uniquename = "
                + "\"" + shape.getUniqueName() + "\";";
        db.execSQL(queryStr);
    }

    public void deletePage(String uniquename) {
        if (db == null) return;
        String deletePage = "DELETE FROM pages WHERE uniquename = "
                + "\"" + uniquename + "\";";
        db.execSQL(deletePage);

        String deleteShape = "DELETE FROM shapes WHERE page = "
                + "\"" + uniquename + "\";";
        db.execSQL(deleteShape);
    }

    public void deleteGame(String name) {
        if (db == null) return;
        Cursor cursorPrev = db.rawQuery(
                "SELECT * FROM games where name = \"" + name + "\";", null);
        String game    = cursorPrev.getString(cursorPrev.getColumnIndex("game"));

        String deleteShape = "DELETE FROM shapes WHERE page in "
                + "(SELECT uniquename from pages WHERE game = \"" + game + "\");";
        db.execSQL(deleteShape);

        String deletePage = "DELETE FROM pages WHERE game = "
                + "\"" + game + "\";";
        db.execSQL(deletePage);

    }
}
