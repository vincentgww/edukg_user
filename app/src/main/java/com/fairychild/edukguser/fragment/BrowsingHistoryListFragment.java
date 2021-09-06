package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.BrowsingHistory;
import com.fairychild.edukguser.datastructure.BrowsingHistoryListFragmentRefreshNotice;
import com.fairychild.edukguser.datastructure.FavouritesListFragmentRefreshNotice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class BrowsingHistoryListFragment extends Fragment {

    public interface DataBaseListener {
        ArrayList<BrowsingHistory> getBrowsingHistory();
        void show_detail_fragment(String label,String course);
    }

    private ListView listView;

    private ArrayList<BrowsingHistory> mData = new ArrayList<BrowsingHistory>();

    private BrowsingHistoryListFragment.DataBaseListener listener;

    private BrowsingHistoryListAdapter adapter;

    public BrowsingHistoryListFragment() {
    }

    public static BrowsingHistoryListFragment newInstance() {
        BrowsingHistoryListFragment fragment = new BrowsingHistoryListFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DataBaseListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_browsing_history_list, container, false);

        listView = view.findViewById(R.id.browsing_history_list_view);
        adapter = new BrowsingHistoryListAdapter(getActivity());
        mData = listener.getBrowsingHistory();
        adapter.setData(mData, false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BrowsingHistory browsingHistory = adapter.getItem(i);
                String name = browsingHistory.getName();
                String course = browsingHistory.getCourse();
                listener.show_detail_fragment(name, course);
            }
        });

        return view;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BrowsingHistoryListFragmentRefreshNotice notice) {
        Log.d("BrowsingHistoryListFragment", "onMessageEvent BrowsingHistoryListFragmentRefreshNotice");
        Toast.makeText(getContext(), "onMessageEvent BrowsingHistoryListFragmentRefreshNotice", Toast.LENGTH_SHORT).show();
        try {
            try{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setData(mData,true);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
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
}