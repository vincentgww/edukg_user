package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.fairychild.edukguser.Activity.SearchActivity;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Knowledge;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultListFragment extends Fragment {

    private Integer size;
    private ArrayList<Knowledge> content;

    private ListView listView;

    private Spinner sortOrderSpinner;

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

        sortOrderSpinner = view.findViewById(R.id.sort_order_spinner);
        sortOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String order = adapterView.getItemAtPosition(i).toString();
                if (order.equals("标题长度")) {
                    changeSortOrder(1);
                } else if (order.equals("类别名长度")){
                    changeSortOrder(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView = view.findViewById(R.id.result_list_view);
        SearchResultListAdapter adapter = new SearchResultListAdapter(getActivity());
        adapter.setData(content, false);
        listView.setAdapter(adapter);
        return view;
    }

    public void changeSortOrder(int mode) {
        SearchResultListAdapter adapter = (SearchResultListAdapter) listView.getAdapter();
        switch (mode) {
            case 1:
                content.sort(new Comparator<Knowledge>() {
                    @Override
                    public int compare(Knowledge knowledge, Knowledge t1) {
                        return Integer.compare(knowledge.getLabel().length(), t1.getLabel().length());
                    }
                });
                adapter.setData(content, true);
                break;
            case 2:
                content.sort(new Comparator<Knowledge>() {
                    @Override
                    public int compare(Knowledge knowledge, Knowledge t1) {
                        return Integer.compare(knowledge.getCategory().length(), t1.getCategory().length());
                    }
                });
                adapter.setData(content, true);
                break;
        }
    }
}