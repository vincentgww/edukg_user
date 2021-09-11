package com.fairychild.edukguser.fragment;

import static com.fairychild.edukguser.R.color.mtrl_text_btn_text_color_selector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairychild.edukguser.MainActivity;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Knowledge;
import com.fairychild.edukguser.sql.UserDatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class SearchResultListAdapter extends BaseAdapter {

    public interface myListener {
        void show_detail_fragment(String label,String course,int back_id);
        SQLiteDatabase getSQLiteDatabase();
    }

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private SearchResultListAdapter.myListener listener;
    private SQLiteDatabase db;

    private ArrayList<Knowledge> mData = new ArrayList<Knowledge>();
    private String course;

    public SearchResultListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.listener=(SearchResultListAdapter.myListener) context;
        db = listener.getSQLiteDatabase();
    }

    public void setData(ArrayList<Knowledge> data, String course, boolean reset) {
        if (reset) {
            this.mData.clear();
        }
        if (data != null) {
            this.mData.addAll(data);
        }
        this.course = course;
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
        public MaterialCardView cardSearchResult;
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
            holder.cardSearchResult = view.findViewById(R.id.search_result_card);
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
                listener.show_detail_fragment(knowledge.getLabel(),course,8);
            }
        });

        if (db != null) {
            Cursor cursor = db.query("DETAIL", new String[]{"NAME"},
                    "COURSE=? AND NAME=?", new String[]{course, knowledge.getLabel()},
                    null, null, null);
            if (cursor.moveToFirst()) {
                Log.d("SearchResultListAdapter", knowledge.getLabel() + " in cache");
                holder.cardSearchResult.setBackgroundColor(Color.parseColor("#DDDDDD"));
            } else {
                Log.d("SearchResultListAdapter", knowledge.getLabel() + " not in cache");
                holder.cardSearchResult.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        return view;
    }
    public void getSubject(String course){
        this.course=course;
    }
}
