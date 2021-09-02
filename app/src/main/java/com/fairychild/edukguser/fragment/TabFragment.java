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

import com.fairychild.edukguser.OkHttp;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.SubItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends ListFragment implements OnScrollListener {
    //private View mView;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tab,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView=getListView();
        loadmoreView=getLayoutInflater().inflate(R.layout.load_more,null);
        loadMoreButton=(Button)loadmoreView.findViewById(R.id.loadBtn);
        listView.addFooterView(loadmoreView);
        mactivity=getActivity();
        EventBus.getDefault().register(this);
        initAdapter();
        setListAdapter(mAdapter);
        listView.setOnScrollListener(this);
    }

    public static TabFragment newInstance(){
        TabFragment indexFragment = new TabFragment();
        return indexFragment;
    }

    private void initAdapter(){
        items=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://47.93.101.225:8000/data"+"?course="+cur_subject+"&page="+cur_page;
                try{
                    String response= OkHttp.get(url);
                    JSONObject jsonObject = new JSONObject(response);
                    try{
                        String data = jsonObject.getString("data");
                        JSONObject dataJSON = new JSONObject(data);
                        ArrayList<String> datalist= (ArrayList<String>) dataJSON.get("page");
                        for(int i=0;i<datalist.size();i++){
                            JSONObject dJson=new JSONObject(datalist.get(i));
                            String cur_entity=dJson.getString("entity_name");
                            SubItem cur_item=new SubItem(cur_entity,cur_subject,cur_subject);
                            items.add(cur_item);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        mAdapter=new SubItemAdapter(mactivity,items);
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
    public void loadMore(View view){
        loadMoreButton.setText("loading...");
        cur_page++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://47.93.101.225:8000/data"+"?course="+cur_subject+"&page="+cur_page;
                try{
                    String response= OkHttp.get(url);
                    JSONObject jsonObject = new JSONObject(response);
                    try{
                        String data = jsonObject.getString("data");
                        JSONObject dataJSON = new JSONObject(data);
                        ArrayList<String> datalist= (ArrayList<String>) dataJSON.get("page");
                        for(int i=0;i<datalist.size();i++){
                            JSONObject dJson=new JSONObject(datalist.get(i));
                            String cur_entity=dJson.getString("entity_name");
                            SubItem cur_item=new SubItem(cur_entity,cur_subject,cur_subject);
                            items.add(cur_item);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
                listView.setSelection(visibleLastIndex - visibleItemCount + 1);
                loadMoreButton.setText("Load more");
            }
        });
    }

    @Subscribe
    public void onEvent(String data){
        cur_subject=data;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
