package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fairychild.edukguser.Item;
import com.fairychild.edukguser.ItemAdapter;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private List<Item> itemList1 = new ArrayList<>();
    private List<Item> itemList2 = new ArrayList<>();
    private RecyclerView itemRecyclerView1;
    private RecyclerView itemRecyclerView2;
    private ItemAdapter adapter1;
    private ItemAdapter adapter2;
    public interface detailListener {
        void getDetail(String entity_name, String course);
    }
    detailListener listener;
    String name,course;
    DetailFragment(String entity_name, String course){
        super();
        name = entity_name;
        this.course = course;
    }

    public void addNewItems(){
        /*Item itm = new Item(label,des,subject);
        if(subject){
            itemList1.add(itm);
            adapter1.notifyItemInserted(itemList1.size()-1);
            msgRecyclerView.scrollToPosition(msgList.size()-1);
        }
        itemList.add(message);
        adapter.notifyItemInserted(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size()-1);*/
        adapter1.notifyItemInserted(itemList1.size()-1);
        itemRecyclerView1.scrollToPosition(itemList1.size()-1);
        adapter2.notifyItemInserted(itemList2.size()-1);
        System.out.println(itemList1.size());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        try {
            JSONObject obj = new JSONObject(event.getMessage());
            JSONObject j = new JSONObject(obj.getString("data"));
            JSONArray arr = new JSONArray(j.getString("property"));
            JSONArray arr2 = new JSONArray(j.getString("content"));
            for(int i=0;i<arr.length();i++){
                JSONObject o = arr.getJSONObject(i);
                String label = o.getString("predicateLabel");
                String des;
                des = o.getString("object");
                if(des.charAt(0)!='h')
                    itemList2.add(new Item(label,des,false));
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
            EventBus.getDefault().removeStickyEvent(event);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView entity_text= view.findViewById(R.id.entity_name);
        entity_text.setText(name);
        listener.getDetail(name, course);
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
        return view;
    }

    @Override
    public void onAttach(Context context) {
        listener = (detailListener) context;
        super.onAttach(context);
    }


    public static DetailFragment newInstance(String entity_name, String course){
        DetailFragment indexFragment = new DetailFragment(entity_name, course);
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
