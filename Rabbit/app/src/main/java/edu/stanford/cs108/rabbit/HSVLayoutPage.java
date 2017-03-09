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

import java.util.Map;

/**
 * Created by qianyu on 2017/3/4.
 */

public class HSVLayoutPage extends LinearLayout {
    private Context mContext;

    public HSVLayoutPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setAdapter(HSVAdapterPage adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            final Map<String, Object> map = adapter.getItem(i);
            View view = adapter.getView(i, null, null);
            view.setPadding(10, 0, 10, 0);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(map.get("index"));
                    int index = (Integer) map.get("index");
//                    HorizontalScrollView hsv = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.hsv_page);
//                    hsv.setVisibility(View.INVISIBLE);


                    final EditView editView = (EditView) ((Activity) getContext()).findViewById(R.id.editView);
                    // Insert new page
                    if (index == 0) {
                        final EditText editText = new EditText(getContext());
                        final int newPageIndex = editView.pageList.size() + 1;
                        editText.setText("Page" + newPageIndex);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Rename");
                        builder.setView(editText);
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editView.insertPage(editText.getText().toString());
                                editView.setCurPageName(editText.getText().toString());
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editView.insertPage("Page" + newPageIndex);
                                editView.setCurPageName("Page" + newPageIndex);
                            }
                        });
                        builder.setCancelable(true);
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();

                        editView.createNewEmptyPage();
                        HorizontalScrollView hsvPage = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.hsv_page);
                        hsvPage.setVisibility(View.INVISIBLE);
                    } else {
                        // choose page
                        int indexInList = index - 1;
                        Page newPage = editView.gameDatabase.getPage(editView.getCurGameName() + editView.pageList.get(indexInList));
                        editView.setCurPageName(editView.pageList.get(indexInList));
                        editView.updateCurPage(newPage);
                    }

                }
            });
            view.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final EditView editView = (EditView) ((Activity) getContext()).findViewById(R.id.editView);
                    editView.popupWindowMain.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden),0,0);
                    return false;
                }
            });
            this.setOrientation(HORIZONTAL);
            this.addView(view, new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}