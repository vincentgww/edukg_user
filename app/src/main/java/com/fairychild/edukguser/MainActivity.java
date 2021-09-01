package com.fairychild.edukguser;
import androidx.fragment.app.FragmentManager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fairychild.edukguser.fragment.FunctionFragment;
import com.fairychild.edukguser.fragment.KnowledgeCheckFragment;
import com.fairychild.edukguser.fragment.MeFragment;
import com.fairychild.edukguser.fragment.QaFragment;
import com.fairychild.edukguser.fragment.HomeFragment;
import com.fairychild.edukguser.fragment.LoginFragment;
import com.fairychild.edukguser.fragment.RegisterFragment;

import com.fairychild.edukguser.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MeFragment.FragmentListener,LoginFragment.LoginListener, QaFragment.QaListener, FunctionFragment.FunctionListener ,
        KnowledgeCheckFragment.KnowledgeCheckListener, RegisterFragment.RegisterListener, SearchFragment.FragmentListener, HomeFragment.FragmentListener {

    //组件
    private BottomNavigationView mBottomNavigationView;
    //Chip Group
    private String id;

    private List<Fragment> mFragments;
    private FragmentTransaction transaction;
    private FragmentManager mSupportFragmentManager;
    private Fragment currentFragment;

    private final String loginUrl = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
    private final String qaUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
    private final String searchPointUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";

    private boolean firstInit = true;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 组件捆绑
        initElement();

        // 获取轻量缓存
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        // 获取接口许可id
        getId();

        System.out.println(0);

        // 初始化fragments列表
        initFragments();

        System.out.println(1);

        // 设置底部导航栏监听器
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        System.out.println(2);

        // 获取fragment管理器
        mSupportFragmentManager = getSupportFragmentManager();

        System.out.println(3);

        switchFragments(0);

        System.out.println(4);
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
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
    };

    private void initElement(){
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.activity_main_bottom_navigation_view);
    }

    private void initFragments(){
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(QaFragment.newInstance());
        mFragments.add(FunctionFragment.newInstance());
        mFragments.add(MeFragment.newInstance());
        mFragments.add(LoginFragment.newInstance());
        mFragments.add(RegisterFragment.newInstance());
        mFragments.add(KnowledgeCheckFragment.newInstance());
        mFragments.add(SearchFragment.newInstance());
    }

    private void switchFragments(int FragmentId) {
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = mFragments.get(FragmentId);
        transaction.addToBackStack(null).replace(R.id.main_frame_layout, targetFragment).commit();
    }

    public void switchToHome() {switchFragments(0);}
    public void switchToQa() {switchFragments(1);}
    public void switchToFunction() {switchFragments(2);}
    public void switchToMe() {switchFragments(3);}
    @Override
    public void switchToLogin(){
        switchFragments(4);
    }
    public void switchToRegister() {switchFragments(5);}
    public void switchToKnowledgeCheck(){
        switchFragments(6);
    }
    public void switchToSearch() {
        switchFragments(7);
    }
    public void switchToBrowsingHistory() {
        switchFragments(8);
    }
    public void switchToFavourites() {
        switchFragments(9);
    }
    public void switchToReport() {
        switchFragments(10);
    }

    @Override
    public void onBackPressed() {
        System.out.println("MainActivity:onBackPressed");
        if (mFragments.size() >= 1) {
            mSupportFragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public String getIdFromSP() {
        return sharedPreferences.getString("id", null);
    }
    public String getPhoneFromSP() {
        return sharedPreferences.getString("phone", null);
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
                    JSONObject jsonObject = new JSONObject(response);
                    try{
                        String data = jsonObject.getString("data");
                        JSONObject dataJSON = new JSONObject(data);
                        id = dataJSON.getString("id");
                        sharedPreferencesEditor.putString("id", id);
                        sharedPreferencesEditor.putString("phone", phone.getText().toString());
                        sharedPreferencesEditor.putString("password", password.getText().toString());
                        sharedPreferencesEditor.apply();
                    } catch(Exception e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this, "连接服务器失败，请重新打开APP!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Toast toast = Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Toast toast = Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        }).start();
    }

    public void checkReg(TextInputEditText phone, TextInputEditText password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://47.93.101.225:8080/register";
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
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
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

    public void sendInfo(String course, String inputQuestion){
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                String url = qaUrl;
                String json = "{\n" +
                        " \"course\":\"" + course + "\",\n" +
                        " \"inputQuestion\":\"" + inputQuestion + "\",\n" +
                        " \"id\":\"" + id + "\"\n" +
                        "}";
                try {
                    String response = OkHttp.post(url, json);
                    EventBus.getDefault().post(new MessageEvent(response));
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
    }

    private void getId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = loginUrl;
                String phone = sharedPreferences.getString("phone", null);
                String password = sharedPreferences.getString("password", null);
                if (phone != null && password != null) {
                    String json = "{\n" +
                            " \"phone\":\"" + "15272961269" + "\",\n" +
                            " \"password\":\"" + "lcs84615" + "\"\n" +
                            "}";
                    System.out.println(json);
                    try {
                        String response = OkHttp.post(url, json);
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        try{
                            id = jsonObject.getString("id");
                            sharedPreferencesEditor.putString("id", id);
                            sharedPreferencesEditor.apply();
                        } catch(Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "连接服务器失败，请重新打开APP!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
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
            }
        }).start();
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

    public void search_point(String s,String course){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = searchPointUrl;
                String json = "{\n" +
                        " \"context\":\"" + s + "\",\n" +
                        " \"course\":\"" + course + "\",\n" +
                        " \"id\":\"" + id + "\"\n" +
                        "}";
                try {
                    String response = OkHttp.post(url, json);
                    EventBus.getDefault().post(new MessageEvent(response));
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

    }
}