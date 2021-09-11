package com.fairychild.edukguser;
import com.fairychild.edukguser.Msg;
import com.fairychild.edukguser.fragment.MeFragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> mMsgList;
    private static Resources resources;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        //ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        //LayoutInflat.from()从一个Context中，获得一个布局填充器，这样你就可以使用这个填充器来把xml布局文件转为View对象了。
        //LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);这样的方法来加载布局msg_item.xml
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
       Msg msg =mMsgList.get(position);
        if(msg.getType()== Msg.TYPE_RECEIVED){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if(msg.getType()== Msg.TYPE_SENT){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
            holder.rightImg.setImageDrawable(null);
            if(MeFragment.head_id==1){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head12);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
            if(MeFragment.head_id==2){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head11);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
            if(MeFragment.head_id==3){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head14);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
            if(MeFragment.head_id==4){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head13);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
            if(MeFragment.head_id==5){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head3);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
            if(MeFragment.head_id==6){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head4);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
            if(MeFragment.head_id==7){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head9);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
            if(MeFragment.head_id==8){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head10);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
            if(MeFragment.head_id==9){
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head15);
                Drawable drawable = new CircleImageViewDrawable(bitmap);
                holder.rightImg.setImageDrawable(drawable);
            }
        }
    }
    @Override
    public int getItemCount(){
        return mMsgList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        RelativeLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView rightImg;
        public ViewHolder(@NonNull View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
            rightImg = view.findViewById(R.id.iv_head_my);
        }
    }
    public MsgAdapter (List<Msg> msgList,Resources res){
        mMsgList = msgList;
        resources=res;
    }
}

