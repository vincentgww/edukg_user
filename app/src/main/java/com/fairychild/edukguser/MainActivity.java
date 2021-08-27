package com.fairychild.edukguser;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.fairychild.edukguser.HomeFragment;
import com.fairychild.edukguser.LoginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements MeFragment.FragmentListener,LoginFragment.LoginListener{
    List<Fragment> mFragments;
    //组件
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    //适配器
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    //Chip Group
    private MenuItem menuItem;
    private FragmentTransaction transaction;
    private FragmentManager mSupportFragmentManager;
    private Fragment currentFragment;
    private boolean firstInit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElement();
        initFragments();
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mSupportFragmentManager = getSupportFragmentManager();
        switchFragments(0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragments(0);
                    return true;
                case R.id.navigation_query:
                    switchFragments(1);
                    return true;
                case R.id.navigation_functions:
                    switchFragments(2);
                    return true;
                case R.id.navigation_me:
                    switchFragments(3);
                    return true;
            }
            return false;
        }
    };

    private void initElement(){
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.activity_main_bottom_navigation_view);
    }

    private void initFragments(){
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(MeFragment.newInstance());
        mFragments.add(LoginFragment.newInstance());
        mFragments.add(BrowsingHistoryFragment.newInstance(10));
        mFragments.add(FavouriteFragment.newInstance(10));
        mFragments.add(ReportFragment.newInstance());
    }

    private void switchFragments(int FragmentId) {
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = mFragments.get(FragmentId);
        if (!targetFragment.isAdded()) {
           transaction.add(R.id.frameLayout,targetFragment);
        }
        /*for (Fragment frag : mFragments) {

            if (frag.equals(targetFragment)) {
                transaction.show(frag);
            }
            else
                transaction.hide(frag);
        }*/
        if(!firstInit)
            transaction.hide(currentFragment);
        firstInit = false;
        transaction.show(targetFragment);
        transaction.commit();
        currentFragment = targetFragment;
    }

    @Override
    public void onBackPressed() {
        if (mFragments.size() >= 1) {
            //显示最顶部那一个
            showFragment(mFragments.get(mFragments.size()-1));
        }else {
            System.out.println(mFragments.size());
            System.out.println("finish!!!!!");
            finish();
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment f : mFragments) {
            fragmentTransaction.hide(f);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void switchToLogin(){
        switchFragments(4);
    }

    @Override
    public void switchToBrowsingHistory() {
        switchFragments(5);
    }

    @Override
    public void switchToFavourites() {
        switchFragments(6);
    }

    @Override
    public void switchToReport() {
        switchFragments(7);
    }

    public void check(EditText phone, EditText password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://47.93.101.225:8080/login";
                String json = "{\n" +
                        " \"account\":\"" + phone.getText() + "\",\n" +
                        " \"password\":\"" + password.getText() + "\"\n" +
                        "}";
                try {
                    String response = OkHttp.post(url, json);
                    System.out.println(response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            switchFragments(3);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}