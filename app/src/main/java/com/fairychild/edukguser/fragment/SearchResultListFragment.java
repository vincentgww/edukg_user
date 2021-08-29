package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Knowledge;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultListFragment extends Fragment {

    private Integer size;
    private ArrayList<Knowledge> content;

    private ListView listView;

    public SearchResultListFragment() {
    }
    public static SearchResultListFragment newInstance(Integer size, ArrayList<Knowledge> content) {
        SearchResultListFragment fragment = new SearchResultListFragment();
        Bundle args = new Bundle();
        args.putInt("size", size);
        args.putParcelableArrayList("content", content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            size = getArguments().getInt("size");
            content = getArguments().getParcelableArrayList("content");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search_result_list, container, false);
        listView = view.findViewById(R.id.result_list_view);
        SearchResultListAdapter adapter = new SearchResultListAdapter(getActivity());
        adapter.setData(content, false);
        listView.setAdapter(adapter);
        return view;
    }
}