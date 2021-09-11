package com.fairychild.edukguser;
import com.fairychild.edukguser.Msg;
import com.fairychild.edukguser.datastructure.outlineItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class outlineAdapter extends RecyclerView.Adapter<outlineAdapter.ViewHolder> {
    private List<outlineItem> mItemList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        //ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
        View view;
        if(viewType==1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outline_title,parent,false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outline_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
        outlineItem item =mItemList.get(position);
        if(getItemViewType(position)==0){
            holder.sub.setText(item.getSub());
            holder.verb.setText(item.getVerb());
            holder.obj.setText(item.getObj());
        }
        else {
            holder.label.setText(item.getSub());
        }
    }
    @Override
    public int getItemCount(){
        return mItemList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView sub;
        TextView label;
        TextView verb;
        TextView obj;
        public ViewHolder(@NonNull View view){
            super(view);
            sub = view.findViewById(R.id.outline_label);
            verb = view.findViewById(R.id.outline_verb);
            obj = view.findViewById(R.id.outline_des);
            label = view.findViewById(R.id.outline_outline);
        }
    }
    @Override
    public int getItemViewType(int position) {
        outlineItem item = mItemList.get(position);
        if(item.getVerb()==null)
            return 1;
        return 0;
    }

    public outlineAdapter (List<outlineItem> itemList){
        mItemList = itemList;
    }
}

