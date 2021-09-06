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

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Favourite;
import com.fairychild.edukguser.datastructure.FavouritesListFragmentRefreshNotice;
import com.fairychild.edukguser.datastructure.LogoutNotice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class FavouritesListFragment extends Fragment {

    public interface DataBaseListener {
        ArrayList<Favourite> getFavourites();
        void show_detail_fragment(String label,String course);
    }

    private ListView listView;

    private ArrayList<Favourite> mData = new ArrayList<Favourite>();

    private FavouritesListFragment.DataBaseListener listener;

    public FavouritesListAdapter adapter;

    public FavouritesListFragment() {
    }

    public static FavouritesListFragment newInstance() {
        FavouritesListFragment fragment = new FavouritesListFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (FavouritesListFragment.DataBaseListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favourites_list, container, false);

        listView = view.findViewById(R.id.favourites_list_view);
        adapter = new FavouritesListAdapter(getActivity());
        mData = listener.getFavourites();
        adapter.setData(mData, false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Favourite favourite = adapter.getItem(i);
                String name = favourite.getName();
                String course = favourite.getCourse();
                listener.show_detail_fragment(name, course);
            }
        });
        return view;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FavouritesListFragmentRefreshNotice notice) {
        Log.d("FavouritesListFragment", "onMessageEvent FavouritesListFragmentRefreshNotice");
        try {
            adapter.setData(mData, true);

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