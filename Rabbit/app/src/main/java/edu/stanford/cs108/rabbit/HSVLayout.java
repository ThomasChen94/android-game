package edu.stanford.cs108.rabbit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
                    EditView editView = (EditView) ((Activity) getContext()).findViewById(R.id.editView);
                    editView.insertShape(EditActivity.images[index - 1], "", "");
                    editView.invalidate();

                }
            });
            this.setOrientation(HORIZONTAL);
            this.addView(view, new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}