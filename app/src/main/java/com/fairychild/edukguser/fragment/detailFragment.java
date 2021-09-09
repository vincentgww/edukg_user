package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fairychild.edukguser.Item;
import com.fairychild.edukguser.ItemAdapter;
import com.fairychild.edukguser.MainActivity;
import com.fairychild.edukguser.MessageEvent;
import com.fairychild.edukguser.Msg;
import com.fairychild.edukguser.MsgAdapter;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.LogoutNotice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class detailFragment extends Fragment {
    private List<Item> itemList1 = new ArrayList<>();
    private List<Item> itemList2 = new ArrayList<>();
    private RecyclerView itemRecyclerView1;
    private RecyclerView itemRecyclerView2;
    private ItemAdapter adapter1;
    private ItemAdapter adapter2;
    private LinearLayout linear;
    private View img;
    private int idx;

    private Button btnQuiz;
    private Button btnAddFavourites;

    public interface detailListener {
        void get_detail(String entity_name, String course);
        void delete_fragment(int idx);
        void addFavourites(String course, String name);
        void removeFavourites(String course, String name);
        boolean isAddedToFavourites(String course, String name);
        void related_question(String name, int idx);
        void back_home();
        void weibo_share(String item_title,String item_content);
    }

    detailListener listener;
    String name,course;
    String simple_description;


    detailFragment(String entity_name, String course, int idx){
        super();
        name = entity_name;
        this.idx = idx;
        this.course = course;
    }

    public void addNewItems(){
        if(itemList1.isEmpty())
            linear.removeView(itemRecyclerView1);
        adapter1.notifyItemInserted(itemList1.size()-1);
        if(itemList2.isEmpty())
            linear.removeView(itemRecyclerView2);
        adapter2.notifyItemInserted(itemList2.size()-1);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        try {
            JSONObject obj = new JSONObject(event.message);
            JSONObject j = new JSONObject(obj.getString("data"));
            JSONArray arr = new JSONArray(j.getString("property"));
            JSONArray arr2 = new JSONArray(j.getString("content"));
            for(int i=0;i<arr.length();i++){
                JSONObject o = arr.getJSONObject(i);
                String label = o.getString("predicateLabel");
                if(label.equals("图片")){
                   Item item = new Item(label, "", false);
                   String url = o.getString("object");
                   item.set_url(url);
                   itemList2.add(item);
                }
                else {
                    String des;
                    des = o.getString("object");
                    if (des.charAt(0) != 'h')
                        itemList2.add(new Item(label, des, false));
                }

            }
            for(int i=0;i<arr2.length();i++){
                JSONObject o = arr2.getJSONObject(i);
                String label = o.getString("predicate_label");
                String des;
                boolean subject = o.has("subject");
                if(subject){
                    des = o.getString("subject_label");
                    if(des.charAt(0)!='h')
                        itemList1.add(new Item(label,des,true));
                }
                else{
                    des = o.getString("object");
                    if(des.charAt(0)!='h')
                        itemList1.add(new Item(label,des,false));
                }
            }
            addNewItems();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView entity_text= view.findViewById(R.id.entity_name);
        linear = view.findViewById(R.id.detail_linear);
        entity_text.setText(name);
        listener.get_detail(name, course);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager1 = new
                        LinearLayoutManager(getActivity());
                itemRecyclerView1= view.findViewById(R.id.detail_recycler1);
                itemRecyclerView1.setLayoutManager(layoutManager1);
                adapter1 = new ItemAdapter(itemList1);
                itemRecyclerView1.setAdapter(adapter1);
                LinearLayoutManager layoutManager2 = new
                        LinearLayoutManager(getActivity());
                itemRecyclerView2= view.findViewById(R.id.detail_recycler2);
                itemRecyclerView2.setLayoutManager(layoutManager2);
                adapter2 = new ItemAdapter(itemList2);
                itemRecyclerView2.setAdapter(adapter2);
            }
        });
        btnQuiz = view.findViewById(R.id.btn_quiz);
        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.related_question(name,idx);
            }
        });
        Button back_button = view.findViewById(R.id.back_button_detail);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.back_home();
                listener.delete_fragment(idx);
            }
        });
        Button mShareButton=view.findViewById(R.id.detail_share_btn);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                simple_description为当前entity的简要介绍
                 */
                simple_description="名称："+name+"\n";
                simple_description+="学科："+course+"\n关系：\n";
                for(Item i:itemList1){
                    String cha = i.get_sub()? "←":"→";
                    simple_description+=i.get_label()+cha+i.get_des()+"\n";
                }
                simple_description+="属性：\n";
                for(Item i:itemList1){
                    simple_description+=i.get_label()+"→"+i.get_des()+"\n";
                }
                listener.weibo_share(name,simple_description);
            }
        });
        btnAddFavourites = view.findViewById(R.id.btn_add_favourites);
        if (listener.isAddedToFavourites(course, name)) {
            btnAddFavourites.setText("取消收藏");
            btnAddFavourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.removeFavourites(course, name);
                    btnAddFavourites.setText("加入收藏");
                }
            });
        } else {
            btnAddFavourites.setText("加入收藏");
            btnAddFavourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.addFavourites(course, name);
                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        listener = (detailListener) context;
        //mActivity = (MainActivity) context;
        super.onAttach(context);
    }


    public static detailFragment newInstance(String entity_name, String course, int idx){
        detailFragment indexFragment = new detailFragment(entity_name, course, idx);
        return indexFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
