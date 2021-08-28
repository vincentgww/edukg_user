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
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.fairychild.edukguser.QaFragment;
import com.fairychild.edukguser.HomeFragment;
import com.fairychild.edukguser.LoginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MeFragment.FragmentListener,LoginFragment.LoginListener, QaFragment.QaListener {
    List<Fragment> mFragments;
    //组件
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    //适配器
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    //Chip Group
    private MenuItem menuItem;
    private String id;
    private FragmentTransaction transaction;
    private FragmentManager mSupportFragmentManager;
    private Fragment currentFragment;
    private final String loginUrl = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
    private final String qaUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
    private String str;
    private boolean firstInit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElement();
        initFragments();
        getId();
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mSupportFragmentManager = getSupportFragmentManager();
        //QaFragment a = (QaFragment) mSupportFragmentManager.findFragmentById(1);
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
        mFragments.add(QaFragment.newInstance());
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
    public String sendInfo(String course, String inputQuestion){
        str=null;
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                String url = qaUrl;
                String json = "{\n" +
                        " \"course\":\"" + course + "\",\n" +
                        " \"inputQuestion\":\"" + inputQuestion + "\",\n" +
                        " \"id\":\"" + id + "\"\n" +
                        "}";
                //System.out.println(json);
                try {
                    String response = OkHttp.post(url, json);
                    //System.out.println(response);
                    //JSONObject jsonObject = new JSONObject(response);
                    //str = response
                    EventBus.getDefault().post(new MessageEvent(response));
                    //qaListener.sendResponse(response);
                    //System.out.println("response1"+jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "发送问题失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
        //System.out.println("response"+str);
        return str;
    }

    private void getId(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = loginUrl;
                String json = "{\n" +
                        " \"phone\":\"" + "15272961269" + "\",\n" +
                        " \"password\":\"" + "lcs84615" + "\"\n" +
                        "}";
                try {
                    String response = OkHttp.post(url, json);
                    JSONObject jsonObject = new JSONObject(response);
                    try{
                        id = jsonObject.getString("id");
                    } catch(Exception e){
                        Toast.makeText(MainActivity.this, "连接服务器失败，请重新打开APP!", Toast.LENGTH_SHORT).show();
                    }
                    //System.out.println(id);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "网络连接失败,请重新打开APP", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(MainActivity.this, event.message, Toast.LENGTH_SHORT).show();
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
    /*public void parseJSONWithJSONObject(String jsonData) {  //解析JSON数据函数
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            int jsonLen = jsonArray.length();
            for (int curr = 0; curr < jsonLen; curr++) {
                JSONObject jsonObject = jsonArray.getJSONObject(curr);
                String name = jsonObject.getString("name");
                String msgContent = jsonObject.getString("message");
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("msgContent", msgContent);  //往Bundle中存放数据
                message.setData(bundle);//mes利用Bundle传递数据
                handler.sendMessage(message);//用activity中的handler发送消息
            }
        } catch (Exception e) {
            Looper.prepare();
            Toast.makeText(getActivity(), "解析json错误!", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }*/
}