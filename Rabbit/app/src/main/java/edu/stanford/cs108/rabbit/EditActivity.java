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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class EditActivity extends Activity {
    public static String[] images = {"carrot_icon", "carrot2_icon", "death_icon", "duck_icon", "fire_icon", "textbox_icon"};
    public static String[] sounds = {"carrotcarrotcarrot", "evillaugh", "fire", "hooray", "munch", "munching", "woof"};
    HSVAdapter hsvAdapter;
    HSVLayout hsvLayout;
    HSVAdapterPage hsvAdapterPage;
    HSVLayoutPage hsvLayoutPage;
    static final String DRAWABLE = "drawable";
    protected static final String RAW = "raw";
    HorizontalScrollView hsv;
    HorizontalScrollView hsvPage;
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
            map.put("index", i);
            hsvAdapter.addObject(map);
        }
        hsvLayout.setAdapter(hsvAdapter);



        hsvAdapterPage = new HSVAdapterPage(this);
        hsvLayoutPage = (HSVLayoutPage) findViewById(R.id.select_layout_page);
        final EditView editView = (EditView) findViewById(R.id.editView);

        Map<String, Object> map = new HashMap<>();
        map.put("text", "newpage");
        map.put("index", 0);
        hsvAdapterPage.addObject(map);


        for (int i = 0; i < editView.pageUserList.size(); i++) {
            Map<String, Object> mapTmp = new HashMap<>();
            mapTmp.put("text", editView.pageUserList.get(i));
            mapTmp.put("index", i + 1);
            hsvAdapterPage.addObject(mapTmp);
        }
        hsvLayoutPage.setAdapter(hsvAdapterPage);

        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        hsvPage = (HorizontalScrollView) findViewById(R.id.hsv_page);

        //initSwitchButtonListener();



    }

    public void initSwitchButtonListener() {
        Switch hidden = (Switch) findViewById(R.id.hidden_switch);
        hidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditView editView = (EditView) findViewById(R.id.editView);
                if (isChecked) {
                    editView.getCurShape().setHidden(true);
                } else {
                    editView.getCurShape().setHidden(false);
                }
            }
        });
        Switch movable = (Switch) findViewById(R.id.movable_switch);
        hidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditView editView = (EditView) findViewById(R.id.editView);
                if (isChecked) {
                    editView.getCurShape().setMovable(true);
                } else {
                    editView.getCurShape().setMovable(false);
                }
            }
        });
    }

    public void updatePageList() {
        final EditView editView = (EditView) findViewById(R.id.editView);
        Map<String, Object> mapTmp = new HashMap<>();
        mapTmp.put("text", editView.pageUserList.get(editView.pageUserList.size() - 1));
        mapTmp.put("index", editView.pageUserList.size());
        hsvAdapterPage.addObject(mapTmp);
        hsvLayoutPage.removeAllViews();
        hsvLayoutPage.setAdapter(hsvAdapterPage);
    }


    public void resetPageList() {
        final EditView editView = (EditView) findViewById(R.id.editView);

        hsvAdapterPage = new HSVAdapterPage(this);
        hsvLayoutPage.removeAllViews();
        Map<String, Object> map = new HashMap<>();
        map.put("text", "newpage");
        map.put("index", 0);
        hsvAdapterPage.addObject(map);


        for (int i = 0; i < editView.pageUserList.size(); i++) {
            Map<String, Object> mapTmp = new HashMap<>();
            mapTmp.put("text", editView.pageUserList.get(i));
            mapTmp.put("index", i + 1);
            hsvAdapterPage.addObject(mapTmp);
        }

        hsvLayoutPage.setAdapter(hsvAdapterPage);

    }

    public void showInsertMenu(View view) {
        if (hsv.getVisibility() == View.VISIBLE) {
            hsv.setVisibility(View.INVISIBLE);
        } else {
            if (hsvPage.getVisibility() == View.VISIBLE) hsvPage.setVisibility(View.INVISIBLE);
            hsv.setVisibility(View.VISIBLE);
        }
    }

    public void showPageMenu(View view) {
        if (hsvPage.getVisibility() == View.VISIBLE) {
            hsvPage.setVisibility(View.INVISIBLE);
        } else {
            if (hsv.getVisibility() == View.VISIBLE) hsv.setVisibility(View.INVISIBLE);
            hsvPage.setVisibility(View.VISIBLE);
        }
    }


    public void rename(View view) {
        final EditView editView = (EditView) findViewById(R.id.editView);
        String curID = editView.getCurShape().getName();
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
                    editView.getCurShape().setName(changedID);

                    //editView.gameDatabase.updateShape(editView.getCurShape(), editView.getCurGameName());

                    editView.gameDatabase.updateShape(editView.getCurShape());
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

    public void showScript(View view) {
        EditView editView = (EditView) findViewById(R.id.editView);
        editView.showShapeScript();
    }

    public void onclickTrigger(View view) {
        EditView editView = (EditView) findViewById(R.id.editView);
        if (editView.curShape != null) {
            editView.resetTmpScript();
            editView.tmpScript[0] = "ONCLICK";
            //editView.curShape.rawScript += "ONCLICK,";
        }
        editView.expandOnclickMenu();
    }

    public void onenterTrigger(View view) {
        EditView editView = (EditView) findViewById(R.id.editView);
        if (editView.curShape != null) {
            editView.resetTmpScript();
            editView.tmpScript[0] = "ONENTER";
            //editView.curShape.rawScript += "ONENTER,";
        }
        editView.expandOnenterMenu();
    }

    public void ondropTrigger(View view) {
        final EditView editView = (EditView) findViewById(R.id.editView);
        if (editView.curShape != null) {
            editView.resetTmpScript();
            editView.tmpScript[0] = "ONDROP";
            //editView.curShape.rawScript += "ONDROP,";
        }

        Integer[] shapeImageID = new Integer[images.length];
        for (int i = 0; i < images.length; i++) {
            shapeImageID[i] = getResources().getIdentifier(images[i], DRAWABLE, getPackageName());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Shape to Hide");
        ListAdapter adapter = new ArrayAdapterWithIcon(this, images, shapeImageID);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editView.tmpScript[1] += images[which] + "";
                //editView.getCurShape().rawScript += images[which] + ",";
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        editView.expandOndropMenu();
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

               // editView.gameDatabase.updateShape(editView.getCurShape(), editView.getCurGameName());
                editView.gameDatabase.updateShape(editView.getCurShape());
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


    public void playAction(View view) {
        final EditView editView = (EditView) findViewById(R.id.editView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String oldSoundName = editView.getCurShape().getSoundName();
        builder.setTitle("Choose Sound");
        builder.setSingleChoiceItems(sounds, -1, new DialogInterface.OnClickListener() {
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
                if (!editView.getCurShape().getSoundName().isEmpty()) {
                    editView.tmpScript[2] = "PLAY";
                    editView.tmpScript[3] = editView.getCurShape().getSoundName() + "";
                    editView.flushTmpScriptToRawScript();

                   // editView.gameDatabase.updateShape(editView.getCurShape(), editView.getCurGameName());
                    editView.gameDatabase.updateShape(editView.getCurShape());
                }

//                editView.getCurShape().rawScript += "PLAY,";
//                editView.getCurShape().rawScript += editView.getCurShape().getSoundName() + ",";
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
        editView.getCurShape().setSoundName("");
    }

    public void gotoAction(View view) {
        final EditView editView = (EditView) findViewById(R.id.editView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Shape to Show");
        final String[] pageNameList = new String[editView.pageUserList.size()];
        for (int i = 0; i < editView.pageUserList.size(); i++) {
            pageNameList[i] = editView.pageUserList.get(i);
        }
        builder.setSingleChoiceItems(pageNameList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editView.togoPageSelected = pageNameList[which];
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editView.tmpScript[2] = "TOGO";
                editView.tmpScript[3] = editView.pageUserList.get(which) + "";
                editView.flushTmpScriptToRawScript();

                //editView.gameDatabase.updateShape(editView.getCurShape(), editView.getCurGameName());

                editView.gameDatabase.updateShape(editView.getCurShape());
//                editView.getCurShape().rawScript += "TOGO,";
//                editView.getCurShape().rawScript += editView.pageList + ",";
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
    public void showAction(View view) {

        final EditView editView = (EditView) findViewById(R.id.editView);

        Integer[] shapeImageID = new Integer[editView.shapeList.size()];
        for (int i = 0; i < editView.shapeList.size(); i++) {
            shapeImageID[i] = getResources().getIdentifier(editView.shapeList.get(i).image, DRAWABLE, getPackageName());
        }
        String[] shapeImageName = new String[editView.shapeList.size()];
        for (int i = 0; i < editView.shapeList.size(); i++) {
            shapeImageName[i] = editView.shapeList.get(i).name;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Shape to Show");
        ListAdapter adapter = new ArrayAdapterWithIcon(this, shapeImageName, shapeImageID);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editView.tmpScript[2] = "SHOW";
                editView.tmpScript[3] = editView.shapeList.get(which).name + "";
                editView.flushTmpScriptToRawScript();

              //  editView.gameDatabase.updateShape(editView.getCurShape(), editView.getCurGameName());
                editView.gameDatabase.updateShape(editView.getCurShape());
//                editView.getCurShape().rawScript += "SHOW,";
//                editView.getCurShape().rawScript += editView.shapeList.get(which).name + ",";
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    public void hideAction(View view) {

        final EditView editView = (EditView) findViewById(R.id.editView);

        Integer[] shapeImageID = new Integer[editView.shapeList.size()];
        for (int i = 0; i < editView.shapeList.size(); i++) {
            shapeImageID[i] = getResources().getIdentifier(editView.shapeList.get(i).image, DRAWABLE, getPackageName());
        }
        String[] shapeImageName = new String[editView.shapeList.size()];
        for (int i = 0; i < editView.shapeList.size(); i++) {
            shapeImageName[i] = editView.shapeList.get(i).name;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Shape to Hide");
        ListAdapter adapter = new ArrayAdapterWithIcon(this, shapeImageName, shapeImageID);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editView.tmpScript[2] = "HIDE";
                editView.tmpScript[3] = editView.shapeList.get(which).name + "";
                editView.flushTmpScriptToRawScript();

                //editView.gameDatabase.updateShape(editView.getCurShape(), editView.getCurGameName());
                editView.gameDatabase.updateShape(editView.getCurShape());
//                editView.getCurShape().rawScript += "HIDE,";
//                editView.getCurShape().rawScript += editView.shapeList.get(which).name + ",";
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }



}
