package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class EditActivity extends Activity {

    public static String[] images = {"carrot", "carrot2", "death", "duck", "fire"};
    public static String[] sounds = {"carrotcarrotcarrot", "evillaugh", "fire", "hooray", "munch", "munching", "woof"};
    private HSVAdapter hsvAdapter;
    private HSVLayout hsvLayout;
    static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_edit);


        hsvAdapter = new HSVAdapter(this);
        hsvLayout = (HSVLayout) findViewById(R.id.select_layout);
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<>();
            int id = getResources().getIdentifier(images[i], DRAWABLE, getPackageName());
            map.put("image", id);
            map.put("index", i + 1);
            hsvAdapter.addObject(map);
        }
        hsvLayout.setAdapter(hsvAdapter);


    }

    public void showInsertMenu(View view) {
        HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        if (hsv.getVisibility() == View.VISIBLE) {
            hsv.setVisibility(View.INVISIBLE);
        } else {
            hsv.setVisibility(View.VISIBLE);
        }
    }


    public void rename(View view) {
        final EditView editView = (EditView) findViewById(R.id.editView);
        String curID = editView.getCurShape().getId();
        final EditText editText = new EditText(this);
        editText.setText(curID);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename");
        builder.setView(editText);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String changedID = editText.getText().toString();
                if (!changedID.isEmpty()) {
                    editView.getCurShape().setId(changedID);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    public void delete(View view) {
        EditView editView = (EditView) findViewById(R.id.editView);
        if (editView.getCurShape() != null) {
            editView.setCurShape(null);
            editView.setCurShapeIndex();
        }
    }

    public void attribute(View view) {
        EditView editView = (EditView) findViewById(R.id.editView);
        editView.expandAttributeMenu();
    }

    public void script(View view) {
        EditView editView = (EditView) findViewById(R.id.editView);
        editView.expandScriptMenu();
    }

    public void setSound(View view) {
        final EditView editView = (EditView) findViewById(R.id.editView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String oldSoundName = editView.getCurShape().getSoundName();
        builder.setTitle("Choose Sound");
        int index = 0;
        for (int i = 0; i < sounds.length; i++) {
            if (sounds[i].equals(oldSoundName)) {
                index = i;
                break;
            }
        }
        builder.setSingleChoiceItems(sounds, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String curSound = sounds[which];
                int soundId = getResources().getIdentifier(curSound, RAW, getPackageName());
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), soundId);
                mp.start();
                editView.getCurShape().setSoundName(curSound);
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editView.getCurShape().setSoundName(oldSoundName);
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        editView.getCurShape().setSoundName("");
    }
}
