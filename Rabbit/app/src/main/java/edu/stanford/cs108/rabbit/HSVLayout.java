package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Created by qianyu on 2017/3/4.
 */

public class HSVLayout extends LinearLayout {
    private Context mContext;

    public HSVLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setAdapter(HSVAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            final Map<String, Object> map = adapter.getItem(i);
            View view = adapter.getView(i, null, null);
            view.setPadding(10, 0, 10, 0);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(map.get("index"));
                    int index = (Integer) map.get("index");
                    HorizontalScrollView hsv = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.hsv);
                    hsv.setVisibility(View.INVISIBLE);

                    // Insert new shape
                    final EditView editView = (EditView) ((Activity) getContext()).findViewById(R.id.editView);
                    int imagesNumber = EditActivity.images.length;
                    if (index == imagesNumber - 1) {
                        // Insert text shape
                        final EditText editText = new EditText(getContext());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Set Text");
                        builder.setView(editText);
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = editText.getText().toString();
                                List<Shape> allShapeList = editView.gameDatabase.getAllShape(editView.getCurGameName());
                                int curShapeCount = allShapeList.size() + 1;
                                String curShapeName = editView.shapeDefaultPrefix + curShapeCount;
                                editView.insertShape(EditActivity.images[0],text, curShapeName,
                                        editView.getCurGameName() + editView.getCurPageName() + curShapeName, editView.getCurGameName() + editView.getCurPageName());
                                editView.invalidate();
                                editView.gameDatabase.addShape(editView.getLastShape());
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setCancelable(true);
                        final AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();


                    } else {
                        // Insert image shape
                        List<Shape> allShapeList = editView.gameDatabase.getAllShape(editView.getCurGameName());
                        int curShapeCount = allShapeList.size() + 1;
                        String curShapeName = editView.shapeDefaultPrefix + curShapeCount;
                        editView.insertShape(EditActivity.images[index], "", curShapeName,
                                editView.pageUniqueList.get(editView.curPageIndex) + curShapeName, editView.pageUniqueList.get(editView.curPageIndex));
                        editView.invalidate();
                        editView.gameDatabase.addShape(editView.getLastShape());

                    }


                }
            });
            this.setOrientation(HORIZONTAL);
            this.addView(view, new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}