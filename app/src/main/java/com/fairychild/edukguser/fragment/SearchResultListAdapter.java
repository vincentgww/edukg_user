package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Knowledge;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SearchResultListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private SubItemAdapter.SubItemAdaptorListener listener;

    private ArrayList<Knowledge> mData = new ArrayList<Knowledge>();
    private String course;

    public SearchResultListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.listener=(SubItemAdapter.SubItemAdaptorListener) context;
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
        public MaterialButton tvDetailButton;
        public MaterialButton tvFavButton;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_search_result, null);
            holder = new ViewHolder();
            holder.tvLabel = view.findViewById(R.id.label);
            holder.tvCategory = view.findViewById(R.id.category);
            holder.tvDetailButton=view.findViewById(R.id.search_detail_btn);
            holder.tvFavButton=view.findViewById(R.id.search_fav_btn);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // 给控件赋值
        Knowledge knowledge = getItem(i);
        holder.tvLabel.setText(knowledge.getLabel());
        holder.tvCategory.setText(knowledge.getCategory());
        holder.tvDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.show_detail_fragment(knowledge.getLabel(),course);
            }
        });

        return view;
    }
    public void getSubject(String course){
        this.course=course;
    }
}
