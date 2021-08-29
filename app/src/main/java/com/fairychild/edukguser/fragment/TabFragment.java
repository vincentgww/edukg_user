package com.fairychild.edukguser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;

public class TabFragment extends Fragment {
    private View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_tab, container, false);
        } else {
            //缓存的mView需要判断是否已经被加过group,如果有group需要从group删除，要不然会发生这个mView已经有group的错误。
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null) {
                group.removeView(mView);
            }
        }
        return mView;
    }

    public static TabFragment newInstance(){
        TabFragment indexFragment = new TabFragment();
        return indexFragment;
    }

}
