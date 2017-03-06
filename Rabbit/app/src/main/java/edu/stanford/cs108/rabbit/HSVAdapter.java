package edu.stanford.cs108.rabbit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qianyu on 2017/3/4.
 */
public class HSVAdapter extends BaseAdapter {
    private List<Map<String, Object>> mList;
    private Context mContext;

    public HSVAdapter(Context context) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.hsv, null);
        ImageView image = (ImageView) view.findViewById(R.id.movie_image);
        Map<String, Object> map = getItem(location);
        image.setBackgroundResource((Integer) map.get("image"));
        return view;
    }
}