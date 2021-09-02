package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.SubItem;

import java.util.List;

public class SubItemAdapter extends BaseAdapter {
    private List<SubItem> items;
    private LayoutInflater inflater;
    public SubItemAdapter(Context context, List<SubItem> items){
        this.items=items;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View view, ViewGroup parent){
        if(view==null){
            view=inflater.inflate(R.layout.sublist_item,null);
        }
        TextView text_1=(TextView) view.findViewById(R.id.item_title);
        text_1.setText(items.get(position).getLabel());
        TextView text_2=(TextView) view.findViewById(R.id.item_description);
        text_2.setText(items.get(position).getDescription());
        return view;
    }

    public void addItem(SubItem item){
        items.add(item);
    }
}
