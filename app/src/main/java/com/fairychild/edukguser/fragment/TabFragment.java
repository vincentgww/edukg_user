package com.fairychild.edukguser.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.fairychild.edukguser.MessageEvent;
import com.fairychild.edukguser.OkHttp;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.SubItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TabFragment extends ListFragment implements OnScrollListener {
    private static final String TAG = "Cannot invoke method length() on null object";
    private ListView listView;
    Activity mactivity;
    private int visibleLastIndex=0;
    private int cur_page=1;
    private int visibleItemCount;
    private View loadmoreView;
    private Button loadMoreButton;
    private List<SubItem> items;
    private SubItemAdapter mAdapter;
    private String cur_subject;
    private View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_tab,container,false);
            mactivity=getActivity();
            //EventBus.getDefault().register(this);
            cur_subject=getArguments().getString("arg").toLowerCase(Locale.ROOT);
            listView=(ListView)rootView.findViewById(android.R.id.list);
            loadmoreView=getLayoutInflater().inflate(R.layout.load_more,null);
            loadMoreButton=(Button)loadmoreView.findViewById(R.id.loadBtn);
            listView.addFooterView(loadmoreView);
            initAdapter();
            try {
                Thread.sleep(300);
            }catch (Exception e){
                e.printStackTrace();
            }
            mAdapter=new SubItemAdapter(mactivity,items);
            listView.setAdapter(mAdapter);
            listView.setOnScrollListener(this);
            loadMoreButton.setOnClickListener(this::LoadMore);
        //}
        return rootView;
    }

    //@Override
    //public void onDestroyView(){
      //  super.onDestroyView();
        //if(rootView!=null){
          //  ((ViewGroup)rootView.getParent()).removeView(rootView);
        //}
    //}

    //@Override
    //public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    //}

    public static TabFragment newInstance(String arg){
        TabFragment indexFragment = new TabFragment();
        Bundle bundle=new Bundle();
        bundle.putString("arg",arg);
        indexFragment.setArguments(bundle);
        return indexFragment;
    }

    private void initAdapter(){
        items=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(cur_subject);
                String url="http://47.93.101.225:8000/data"+"?course="+cur_subject+"&page="+cur_page;
                OkHttpClient okHttpClient=new OkHttpClient();
                Request request=new Request.Builder().get().url(url).build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Toast.makeText(mactivity,"get failed",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"get failed");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String res=response.body().string();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    JSONObject jsonObject=new JSONObject(res);
                                    String data=jsonObject.getString("data");
                                    JSONObject tjson=new JSONObject(data);
                                    JSONArray page=tjson.getJSONArray("page");
                                    //JSONArray page = new JSONArray(page_str);
                                    //JSONArray jsonArray=new JSONArray(page);
                                    for(int i=0;i<page.length();i++){
                                        try {
                                            tjson=page.getJSONObject(i);
                                            String cur_title=tjson.getString("entity_name");
                                            SubItem cur_item=new SubItem(cur_title,cur_subject,cur_subject);
                                            items.add(cur_item);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }
                });

            }
        }).start();
        //mAdapter=new SubItemAdapter(mactivity,items);
    }

    @Override
    public void onScroll(AbsListView view,int firstVisibleItem, int visibleItemCount, int totalItemCount){
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex = mAdapter.getCount() - 1; //数据集最后一项的索引
        int lastIndex = itemsLastIndex + 1;  //加上底部的loadMoreView项
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
            //如果是自动加载,可以在这里放置异步加载数据的代码
            Log.i("LOADMORE", "loading...");
        }
    }
    public void LoadMore(View view){
        loadMoreButton.setText("loading...");
        System.out.println(cur_page);
        cur_page++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(cur_subject);
                String url="http://47.93.101.225:8000/data"+"?course="+cur_subject+"&page="+cur_page;
                OkHttpClient okHttpClient=new OkHttpClient();
                Request request=new Request.Builder().get().url(url).build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Toast.makeText(mactivity,"get failed",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"get failed");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String res=response.body().string();
                        //Log.d(TAG,res);
                        mactivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    JSONObject jsonObject=new JSONObject(res);
                                    String data=jsonObject.getString("data");
                                    JSONObject tjson=new JSONObject(data);
                                    JSONArray page=tjson.getJSONArray("page");
                                    for(int i=0;i<page.length();i++){
                                        try {
                                            tjson=page.getJSONObject(i);
                                            String cur_title=tjson.getString("entity_name");
                                            SubItem cur_item=new SubItem(cur_title,cur_subject,cur_subject);
                                            items.add(cur_item);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    listView.setSelection(visibleLastIndex - visibleItemCount + 1);
                                    loadMoreButton.setText("Load more...");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }
        }).start();
        //loadMoreButton.setText("Load more...");
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event){
//        Log.d(TAG,event.message.toLowerCase(Locale.ROOT));
//        cur_subject=event.message.toLowerCase(Locale.ROOT);
        //Log.d(TAG,cur_subject);
//    }

    //@Override
//    public void onStart(){
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
}
