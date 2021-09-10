package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.LocalCache;
import com.fairychild.edukguser.datastructure.LocalCacheListFragmentRefreshNotice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class LocalCacheListFragment extends Fragment {

    public interface myListener{
        ArrayList<LocalCache> getLocalCacheList();
        void show_detail_fragment(String label,String course);
    }

    private ListView listView;

    private ArrayList<LocalCache> mData = new ArrayList<LocalCache>();

    private LocalCacheListFragment.myListener listener;

    private LocalCacheListAdapter adapter;

    public LocalCacheListFragment() {
    }

    public static LocalCacheListFragment newInstance() {
        LocalCacheListFragment fragment = new LocalCacheListFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (myListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("LocalCacheListFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_local_cache_list, container, false);

        listView = view.findViewById(R.id.local_cache_list_view);

        adapter = new LocalCacheListAdapter(getActivity());
        adapter.setData(mData, false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LocalCache localCache = adapter.getItem(i);
                String name = localCache.getName();
                String course = localCache.getCourse();
                listener.show_detail_fragment(name, course);
            }
        });

        return view;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocalCacheListFragmentRefreshNotice notice) {
        Log.d("LocalCacheListFragment", "onMessageEvent LocalCacheListFragmentRefreshNotice");
        try {
            adapter = new LocalCacheListAdapter(getActivity());
            adapter.setData(mData, false);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    LocalCache localCache = adapter.getItem(i);
                    String name = localCache.getName();
                    String course = localCache.getCourse();
                    listener.show_detail_fragment(name, course);
                }
            });
            EventBus.getDefault().removeStickyEvent(notice);

        } catch(Exception e){
            e.printStackTrace();
        }
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

    public void setmData(ArrayList<LocalCache> mData) {
        this.mData = mData;
    }
}