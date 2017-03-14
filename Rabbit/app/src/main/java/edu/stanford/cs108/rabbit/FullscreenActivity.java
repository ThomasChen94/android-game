package edu.stanford.cs108.rabbit;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen);
    }


    SQLiteDatabase db;
    public void click(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);

    }

    public void editClick(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }


}
