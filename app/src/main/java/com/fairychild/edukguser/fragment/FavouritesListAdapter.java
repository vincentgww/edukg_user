package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Favourite;

import java.util.ArrayList;

public class FavouritesListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private ArrayList<Favourite> mData = new ArrayList<Favourite>();

    public FavouritesListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Favourite> data, boolean reset) {
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
    public Favourite getItem(int i) {
        return this.mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setData(ArrayList<Favourite> mData) {
        this.mData = mData;
    }

    static class ViewHolder{
        public TextView tvCourse;
        public TextView tvName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FavouritesListAdapter.ViewHolder holder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_favourite, null);
            holder = new FavouritesListAdapter.ViewHolder();
            holder.tvCourse = view.findViewById(R.id.tv_favourite_course);
            holder.tvName = view.findViewById(R.id.tv_favourite_name);
            view.setTag(holder);
        } else {
            holder = (FavouritesListAdapter.ViewHolder) view.getTag();
        }

        // 给控件赋值
        Favourite favourite = getItem(i);
        holder.tvCourse.setText(favourite.getCourse());
        holder.tvName.setText(favourite.getName());

        return view;
    }
}
