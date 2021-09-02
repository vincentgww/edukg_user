package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.BrowsingHistory;

import java.util.ArrayList;

public class BrowsingHistoryListFragment extends Fragment {

    public interface DataBaseListener {
        ArrayList<BrowsingHistory> getBrowsingHistory();
    }

    private ListView listView;

    private ArrayList<BrowsingHistory> mData = new ArrayList<BrowsingHistory>();

    private BrowsingHistoryListFragment.DataBaseListener listener;

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
        BrowsingHistoryListAdapter adapter = new BrowsingHistoryListAdapter(getActivity());
        mData = listener.getBrowsingHistory();
        adapter.setData(mData, false);
        listView.setAdapter(adapter);

        return view;
    }
}