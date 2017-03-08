package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qianyu on 2017/3/4.
 */
public class HSVAdapterPage extends BaseAdapter {
    private List<Map<String, Object>> mList;
    private Context mContext;

    public HSVAdapterPage(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Map<String, Object> getItem(int location) {
        return mList.get(location);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void addObject(Map<String, Object> map) {
        mList.add(map);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int location, View arg1, ViewGroup arg2) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hsv_page, null);
        TextView text = (TextView) view.findViewById(R.id.movie_text);
        Map<String, Object> map = getItem(location);
        String s = (String) map.get("text");
        int id = mContext.getResources().getIdentifier("plus_small_icon", "drawable", mContext.getPackageName());
        if (s.equals("newpage")) {
            text.setMaxWidth(50);
            text.setMaxHeight(50);
            text.setBackgroundResource(id);
        } else {
            text.setText((String) map.get("text"));
            text.setTextSize(30f);
        }

        // text.setBackgroundResource((Integer) map.get("image"));
        return view;
    }
}