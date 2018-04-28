package com.animate.lvweihao.testanimate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by lv.weihao on 2018/4/26.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private List<Map<String, String>> arylist;
    private Context mContext;
    private OnItemClickListenter listenter;
    private boolean isShow;
    private boolean clickable = true;

    public MyRecyclerAdapter(Context mContext, List<Map<String, String>> arylist, boolean isShow) {
        this.mContext = mContext;
        this.arylist = arylist;
        this.isShow = isShow;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void onSwipeChange(boolean isShow) {
        this.isShow = isShow;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListenter listener) {
        this.listenter = listener;
    }

    /**
     * 增加数据
     */
    public void addData(int position, Map<String, String> data) {
        arylist.add(position, data);
        notifyItemInserted(position);//注意这里
    }

    /**
     * 移除数据
     */
    public void removeData(int position) {
        arylist.remove(position);
        notifyItemRemoved(position);//注意这里
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv.setText(arylist.get(position).toString());
        holder.tv2.setText(arylist.get(position).get("OLD_INDEX"));

        ViewGroup.LayoutParams layoutParams = holder.iv.getLayoutParams();
        int ivWidth = 180;
        int ivHeight = 180;
        int toWidth = 108;
        int toHeight = 108;
        if(isShow){
            layoutParams.width = toWidth;
            layoutParams.height = toHeight;
            holder.iv.setLayoutParams(layoutParams);
        } else {
            layoutParams.width = ivWidth;
            layoutParams.height = ivHeight;
            holder.iv.setLayoutParams(layoutParams);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickable) {
                    listenter.onItemClickListener(v, holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arylist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView tv2;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}
