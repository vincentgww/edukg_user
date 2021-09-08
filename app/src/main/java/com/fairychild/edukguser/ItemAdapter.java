package com.fairychild.edukguser;
import com.fairychild.edukguser.Msg;

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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> mItemList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        //ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
        View view;
        if(viewType==1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item,parent,false);
        else if(viewType == 0)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_item,parent,false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_detail,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
        Item item =mItemList.get(position);
        if(getItemViewType(position)>=2){
            new ImageLoadTask(item.get_url(), holder.img).execute();
        }
        else if(item.get_sub()){
            holder.sub_label.setText(item.get_label());
            holder.sub_des.setText(item.get_des());
        }else {
            holder.obj_label.setText(item.get_label());
            holder.obj_des.setText(item.get_des());
        }
    }
    @Override
    public int getItemCount(){
        return mItemList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView obj_label;
        TextView obj_des;
        TextView sub_des;
        TextView sub_label;
        ImageView img;
        public ViewHolder(@NonNull View view){
            super(view);
            obj_des = view.findViewById(R.id.obj_des);
            obj_label = view.findViewById(R.id.obj_label);
            sub_des = view.findViewById(R.id.sub_des);
            sub_label = view.findViewById(R.id.sub_label);
            img = view.findViewById(R.id.image_detail);
        }
    }
    @Override
    public int getItemViewType(int position) {
        Item item = mItemList.get(position);
        if(item.get_url()!=null)
            return position+2;
        if(item.get_sub()) {
            return 1;
        } else{
            return 0;
        }
    }

    public ItemAdapter (List<Item> itemList){
        mItemList = itemList;
    }
}

