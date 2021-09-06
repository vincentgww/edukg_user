package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.fairychild.edukguser.MainActivity;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Knowledge;

import java.util.ArrayList;
import java.util.Comparator;

import info.debatty.java.stringsimilarity.Jaccard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultListFragment extends Fragment {

    public interface DetailListener {
        void show_detail_fragment(String subject, String name);
    }

    private SearchResultListFragment.DetailListener listener;

    private String course;
    private String searchContent;
    private Integer size;
    private ArrayList<Knowledge> content;
    private Jaccard jaccard = new Jaccard();

    private ListView listView;

    private Spinner sortOrderSpinner;

    public SearchResultListFragment() {
    }
    public static SearchResultListFragment newInstance(Integer size, ArrayList<Knowledge> content,
                                                       String course, String searchContent) {
        SearchResultListFragment fragment = new SearchResultListFragment();
        Bundle args = new Bundle();
        args.putInt("size", size);
        args.putString("course", course);
        args.putParcelableArrayList("content", content);
        args.putString("searchContent", searchContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (SearchResultListFragment.DetailListener) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            size = getArguments().getInt("size");
            course = getArguments().getString("course");
            content = getArguments().getParcelableArrayList("content");
            searchContent = getArguments().getString("searchContent");
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
                changeSortOrder(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView = view.findViewById(R.id.result_list_view);
        SearchResultListAdapter adapter = new SearchResultListAdapter(getActivity());
        adapter.setData(content, false);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Knowledge knowledge = adapter.getItem(i);
                listener.show_detail_fragment(knowledge.getLabel(), course);
            }
        });

        return view;
    }

    public void changeSortOrder(int mode) {
        SearchResultListAdapter adapter = (SearchResultListAdapter) listView.getAdapter();
        switch (mode) {
            case 0:
                content.sort(new Comparator<Knowledge>() {
                    @Override
                    public int compare(Knowledge knowledge, Knowledge t1) {
                        int result1 =  Double.compare(jaccard.distance(searchContent, knowledge.getLabel()), jaccard.distance(searchContent, t1.getLabel()));
                        return result1 == 0 ? Double.compare(jaccard.distance(searchContent, knowledge.getCategory()), jaccard.distance(searchContent, t1.getCategory())) : result1;
                    }
                });
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