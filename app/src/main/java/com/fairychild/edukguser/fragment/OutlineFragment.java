package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fairychild.edukguser.Item;
import com.fairychild.edukguser.ItemAdapter;
import com.fairychild.edukguser.MainActivity;
import com.fairychild.edukguser.MessageEvent;
import com.fairychild.edukguser.Msg;
import com.fairychild.edukguser.MsgAdapter;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.Share;
import com.fairychild.edukguser.datastructure.DetailMessageEvent;
import com.fairychild.edukguser.datastructure.LogoutNotice;
import com.fairychild.edukguser.datastructure.outlineItem;
import com.fairychild.edukguser.outlineAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OutlineFragment extends Fragment {
    private List<outlineItem> mItemList;
    private RecyclerView itemRecyclerView;
    private outlineAdapter adapter;
    private LinearLayout linear;
    private View img;
    private int idx;

    private Button btnQuiz;
    private Button btnAddFavourites;

    public interface outlineListener {
        void back_function(int id);
    }

    outlineListener listener;
    String label,course;
    int back_id;
    String simple_description;


    OutlineFragment(String label, int id, List<outlineItem> mItemList, int back_id){
        super();
        this.label = label;
        this.idx = id;
        this.mItemList = mItemList;
        this.back_id = back_id;
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outline, container, false);
        //TextView entity_text= view.findViewById(R.id.entity_name);
        //linear = view.findViewById(R.id.detail_linear);
        //entity_text.setText(name);
        //listener.get_detail(name, course);
        Button backBtn = view.findViewById(R.id.back_button_outline);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.back_function(idx);
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new
                        LinearLayoutManager(getActivity());
                itemRecyclerView= view.findViewById(R.id.outline_recycler);
                itemRecyclerView.setLayoutManager(layoutManager);
                adapter = new outlineAdapter(mItemList);
                itemRecyclerView.setAdapter(adapter);
                adapter.notifyItemInserted(mItemList.size()-1);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        listener = (outlineListener) context;
        //mActivity = (MainActivity) context;
        super.onAttach(context);
    }


    public static OutlineFragment newInstance(String label, int id, List<outlineItem> mItemList, int idx){
        OutlineFragment indexFragment = new OutlineFragment(label, id, mItemList, idx);
        return indexFragment;
    }

}
