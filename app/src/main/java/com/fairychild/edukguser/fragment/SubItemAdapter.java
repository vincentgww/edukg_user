package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.SubItem;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SubItemAdapter extends BaseAdapter {
    public interface SubItemAdaptorListener{
        void show_detail_fragment(String label,String course);
    }
    static class ViewHolder{
        TextView label_text;
        TextView description_text;
        MaterialButton detaiBtn;
        MaterialButton likeBtn;
    }
    private SubItemAdaptorListener listener;
    private List<SubItem> items;
    private LayoutInflater inflater;
    public SubItemAdapter(Context context, List<SubItem> items){
        this.items=items;
        //this.listener=listener;
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
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.sublist_item,null);
            holder=new ViewHolder();
            holder.label_text=(TextView) convertView.findViewById(R.id.item_title);
            holder.description_text=(TextView) convertView.findViewById(R.id.item_description);
            holder.detaiBtn=(MaterialButton)convertView.findViewById(R.id.detailBtn);
            holder.likeBtn=(MaterialButton)convertView.findViewById(R.id.likeBtn);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.label_text.setText(items.get(position).getLabel());
        holder.description_text.setText(items.get(position).getDescription());
        holder.detaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.show_detail_fragment(holder.label_text.toString(),holder.description_text.toString());
            }
        });
        return convertView;
    }

    public void addItem(SubItem item){
        items.add(item);
    }
}
