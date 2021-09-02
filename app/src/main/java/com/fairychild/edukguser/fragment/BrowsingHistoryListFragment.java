package com.fairychild.edukguser.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fairychild.edukguser.R;

public class BrowsingHistoryListFragment extends Fragment {

    public BrowsingHistoryListFragment() {
    }

    public static BrowsingHistoryListFragment newInstance() {
        BrowsingHistoryListFragment fragment = new BrowsingHistoryListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browsing_history_list, container, false);
    }
}