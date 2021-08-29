package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Knowledge;

import java.util.ArrayList;

public class SearchResultListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private ArrayList<Knowledge> mData = new ArrayList<Knowledge>();

    public SearchResultListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Knowledge> data, boolean reset) {
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
    public Knowledge getItem(int i) {
        return this.mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder{
        public TextView tvLabel;
        public TextView tvCategory;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_search_result, null);
            holder = new ViewHolder();
            holder.tvLabel = view.findViewById(R.id.label);
            holder.tvCategory = view.findViewById(R.id.category);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // 给控件赋值
        Knowledge knowledge = getItem(i);
        holder.tvLabel.setText(knowledge.getLabel());
        holder.tvCategory.setText(knowledge.getCategory());

        return view;
    }
}
