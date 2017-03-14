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
import android.widget.TextView;
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
                        final int newPageIndex = editView.pageUserList.size() + 1;
                        editText.setText("Page" + newPageIndex);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Rename");
                        builder.setView(editText);
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                editView.curPageIndex = newPageIndex - 1;
//                                editView.insertPage(editText.getText().toString());
//                                editView.setCurPageName(editText.getText().toString());
//
//
//                                TextView textView1 = (TextView) ((Activity)getContext()).findViewById(R.id.page_textview);
//                                textView1.setText(editView.curGameName + editView.curPageName);
//                                TextView textView2 = (TextView) ((Activity)getContext()).findViewById(R.id.unique_page_textview);
//                                textView2.setText(editView.pageUniqueList.get(editView.curPageIndex));
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                editView.insertPage("Page" + newPageIndex);
//                                editView.setCurPageName("Page" + newPageIndex);
                            }
                        });
                        builder.setCancelable(true);
                        final AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String curNewGameName = editText.getText().toString();
                                if (!editView.gameDatabase.containsPage(curNewGameName)) {
                                    editView.curPageIndex = newPageIndex - 1;
                                    editView.insertPage(editText.getText().toString());
                                    editView.setCurPageName(editText.getText().toString());


                                    TextView textView1 = (TextView) ((Activity)getContext()).findViewById(R.id.page_textview);
                                    textView1.setText(editView.curGameName + editView.curPageName);
                                    TextView textView2 = (TextView) ((Activity)getContext()).findViewById(R.id.unique_page_textview);
                                    textView2.setText(editView.pageUniqueList.get(editView.curPageIndex));

                                    dialog.dismiss();
                                } else {
                                    final EditView editView = (EditView) findViewById(R.id.editView);
                                    AlertDialog.Builder builderWarning = new AlertDialog.Builder(getContext());
                                    builderWarning.setTitle("Duplicated Game Name");
                                    builderWarning.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builderWarning.setCancelable(true);
                                    AlertDialog dialogWarning = builderWarning.create();
                                    dialogWarning.setCanceledOnTouchOutside(false);
                                    dialogWarning.show();
                                }
                            }
                        });


                        editView.createNewEmptyPage();
                        HorizontalScrollView hsvPage = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.hsv_page);
                        hsvPage.setVisibility(View.INVISIBLE);



                    } else {
                        // choose page
                        int indexInList = index - 1;
                        Page newPage = editView.gameDatabase.getPage(editView.pageUniqueList.get(indexInList));
                        editView.setCurPageName(editView.pageUserList.get(indexInList));
                        editView.curPageIndex = indexInList;
                        editView.updateCurPage(newPage);
                        TextView textView1 = (TextView) ((Activity)getContext()).findViewById(R.id.page_textview);
                        textView1.setText(editView.curGameName + editView.curPageName);
                        TextView textView2 = (TextView) ((Activity)getContext()).findViewById(R.id.unique_page_textview);
                        textView2.setText(editView.pageUniqueList.get(editView.curPageIndex));
                    }

                }
            });
            view.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final EditView editView = (EditView) ((Activity) getContext()).findViewById(R.id.editView);
                    int index = (Integer) map.get("index");
                    int indexInList = index - 1;
                    if (indexInList >= 1) {
                        editView.setCurPageName(editView.pageUserList.get(indexInList));
                        editView.curPageIndex = indexInList;
                        editView.popupWindowPage.showAsDropDown(((Activity) getContext()).findViewById(R.id.hidden), 0, 0);
                    }
                    return false;
                }
            });
            this.setOrientation(HORIZONTAL);
            this.addView(view, new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}