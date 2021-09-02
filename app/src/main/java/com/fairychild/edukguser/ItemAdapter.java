package com.fairychild.edukguser;
import com.fairychild.edukguser.Msg;

import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_item,parent,false);
        //LayoutInflat.from()从一个Context中，获得一个布局填充器，这样你就可以使用这个填充器来把xml布局文件转为View对象了。
        //LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);这样的方法来加载布局msg_item.xml
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
        Item item =mItemList.get(position);
        if(item.get_sub()){
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
        public ViewHolder(@NonNull View view){
            super(view);
            obj_des = view.findViewById(R.id.obj_des);
            obj_label = view.findViewById(R.id.obj_label);
            sub_des = view.findViewById(R.id.sub_des);
            sub_label = view.findViewById(R.id.sub_label);
        }
    }
    @Override
    public int getItemViewType(int position) {
        Item item = mItemList.get(position);
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

