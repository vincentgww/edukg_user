package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fairychild.edukguser.MainActivity;
import com.fairychild.edukguser.datastructure.MessageEvent;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Knowledge;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    public interface FragmentListener {
        void search(String subject, String searchKey);
    }

    private Spinner subjectSpinner;

    private String subject;

    private EditText searchContent;
    private String searchKey;

    private Button backBtn;
    private Button searchBtn;

    SearchFragment.FragmentListener listener;
    private MainActivity mainActivity;

    private FragmentTransaction transaction;
    private FragmentManager mSupportFragmentManager;
    private SearchResultListFragment currentFragment = new SearchResultListFragment();

    private boolean firstInit = true;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        listener = (SearchFragment.FragmentListener) context;
        mainActivity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBtn = view.findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onBackPressed();
            }
        });

        subjectSpinner = view.findViewById(R.id.subject_spinner);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                subject = null;
            }
        });

        searchContent = view.findViewById(R.id.search_content);

        searchBtn = view.findViewById(R.id.real_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 searchKey = searchContent.getText().toString();
                 listener.search(subject, searchKey);
             }
        });

        mSupportFragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void showFragment(String response) {
        transaction = mSupportFragmentManager.beginTransaction();
        try {
            String data = new JSONObject(response).getString("data");
            System.out.println(data);
            Gson gson = new Gson();
            ArrayList<Knowledge> content = new ArrayList<Knowledge>();
            content = gson.fromJson(data, new TypeToken<ArrayList<Knowledge>>(){}.getType());
            System.out.println(content);
            SearchResultListFragment searchResultListFragment = SearchResultListFragment.newInstance(content, searchKey, subject);
            transaction.add(R.id.search_frame_layout, searchResultListFragment);
            if (!firstInit) {
                transaction.remove(currentFragment);
            }
            firstInit = false;
            transaction.show(searchResultListFragment);
            currentFragment = searchResultListFragment;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        transaction.commit();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        System.out.println(event.getMessage());
        try {
            showFragment(event.getMessage());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}