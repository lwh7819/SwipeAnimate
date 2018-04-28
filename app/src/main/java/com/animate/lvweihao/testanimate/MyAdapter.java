package com.animate.lvweihao.testanimate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by lv.weihao on 2016/9/20.
 */
public class MyAdapter extends BaseAdapter {
    private List<Map<String, String>> arylist;
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isShow;
    private ImageView iv;
    private ViewGroup.LayoutParams layoutParams;
    int ivWidth,ivHeight,toWidth,toHeight;
    public MyAdapter(List<Map<String, String>> list, Context context , boolean isShow){
        this.arylist = list;
        this.context =context;
        this.isShow = isShow;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_listview, null);
            convertView.measure(0, 0);
            holder = new ViewHolder();
//            itemHeight = convertView.getMeasuredHeight();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        layoutParams = holder.iv.getLayoutParams();
        ivWidth = 180;
        ivHeight = 180;
        toWidth = 144;
        toHeight = 144;
        if(isShow){
            layoutParams.width = toWidth;
            layoutParams.height = toHeight;
            holder.iv.setLayoutParams(layoutParams);
        } else {
            layoutParams.width = ivWidth;
            layoutParams.height = ivHeight;
            holder.iv.setLayoutParams(layoutParams);
        }
        holder.tv.setText(getItem(position).toString());
        holder.tv2.setText(arylist.get(position).get("OLD_INDEX"));

        return convertView;
    }

    class ViewHolder {
        TextView tv;
        TextView tv2;
        ImageView iv;
    }
}
