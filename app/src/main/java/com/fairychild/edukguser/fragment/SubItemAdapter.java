package com.fairychild.edukguser.fragment;

import android.annotation.SuppressLint;
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

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.SubItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SubItemAdapter extends BaseAdapter {
    public interface SubItemAdaptorListener{
        void show_detail_fragment(String label,String course);
        SQLiteDatabase getSQLiteDatabase();
    }
    static class ViewHolder{
        TextView label_text;
        TextView description_text;
        MaterialButton detaiBtn;
        MaterialButton likeBtn;
        public MaterialCardView cardSubitem;
    }
    private SubItemAdaptorListener listener;
    private List<SubItem> items;
    private LayoutInflater inflater;
    private SQLiteDatabase db;
    private Context mContext;

    public SubItemAdapter(Context context, List<SubItem> items){
        this.items=items;
        mContext = context;
        listener=(SubItemAdaptorListener)context;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = listener.getSQLiteDatabase();
    }

    @Override
    public int getCount(){
        return items.size();
    }
    @Override
    public Object getItem(int position){
        return items.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.sublist_item,null);
            holder=new ViewHolder();
            holder.label_text=(TextView) convertView.findViewById(R.id.item_title);
            holder.description_text=(TextView) convertView.findViewById(R.id.item_description);
            holder.detaiBtn=(MaterialButton)convertView.findViewById(R.id.detailBtn);
            holder.likeBtn=(MaterialButton)convertView.findViewById(R.id.likeBtn);
            holder.cardSubitem = convertView.findViewById(R.id.subitem);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.label_text.setText(items.get(position).getLabel());
        holder.description_text.setText(items.get(position).getDescription());
        holder.detaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(holder.label_text.getText().toString());
                System.out.println(holder.description_text.getText().toString());
                listener.show_detail_fragment(holder.label_text.getText().toString(),holder.description_text.getText().toString());
            }
        });

        if (db != null) {
            Cursor cursor = db.query("DETAIL", new String[]{"NAME"},
                    "COURSE=? AND NAME=?", new String[]{items.get(position).getCourse(), items.get(position).getLabel()},
                    null, null, null);
            if (cursor.moveToFirst()) {
                Log.d("SubItemAdapter", items.get(position).getLabel() + " in cache");
                holder.cardSubitem.setBackgroundColor(Color.parseColor("#DDDDDD"));
            } else {
                Log.d("SubItemAdapter", items.get(position).getLabel() + " not in cache");
            }
        }

        return convertView;
    }

    public void addItem(SubItem item){
        items.add(item);
    }
}
