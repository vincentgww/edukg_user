package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.BrowsingHistory;

import java.util.ArrayList;

public class BrowsingHistoryListAdapter  extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private ArrayList<BrowsingHistory> mData = new ArrayList<BrowsingHistory>();

    public BrowsingHistoryListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<BrowsingHistory> data, boolean reset) {
        if (reset) {
            this.mData.clear();
        }
        if (data != null) {
            this.mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.mData.size();
    }

    @Override
    public BrowsingHistory getItem(int i) {
        return this.mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder{
        public TextView tvCourse;
        public TextView tvName;
        public TextView tvTime;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BrowsingHistoryListAdapter.ViewHolder holder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_browsing_history, null);
            holder = new BrowsingHistoryListAdapter.ViewHolder();
            holder.tvCourse = view.findViewById(R.id.tv_browsing_course);
            holder.tvName = view.findViewById(R.id.tv_browsing_name);
            holder.tvTime = view.findViewById(R.id.tv_browsing_time);
            view.setTag(holder);
        } else {
            holder = (BrowsingHistoryListAdapter.ViewHolder) view.getTag();
        }

        // 给控件赋值
        BrowsingHistory browsingHistory = getItem(i);
        holder.tvCourse.setText(browsingHistory.getCourse());
        holder.tvName.setText(browsingHistory.getName());
        holder.tvTime.setText(browsingHistory.getTime());

        return view;
    }
}