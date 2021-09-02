package com.fairychild.edukguser;
import androidx.fragment.app.FragmentManager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fairychild.edukguser.datastructure.BrowsingHistory;
import com.fairychild.edukguser.datastructure.LoginNotice;
import com.fairychild.edukguser.datastructure.LogoutNotice;
import com.fairychild.edukguser.datastructure.MessageEvent;
import com.fairychild.edukguser.fragment.BrowsingHistoryListFragment;
import com.fairychild.edukguser.fragment.DetailFragment;
import com.fairychild.edukguser.fragment.FunctionFragment;
import com.fairychild.edukguser.fragment.KnowledgeCheckFragment;
import com.fairychild.edukguser.fragment.MeFragment;
import com.fairychild.edukguser.fragment.QaFragment;
import com.fairychild.edukguser.fragment.HomeFragment;
import com.fairychild.edukguser.fragment.LoginFragment;
import com.fairychild.edukguser.fragment.QuizFragment;
import com.fairychild.edukguser.fragment.RegisterFragment;

import com.fairychild.edukguser.fragment.SearchFragment;
import com.fairychild.edukguser.fragment.SearchResultListFragment;
import com.fairychild.edukguser.sql.UserDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MeFragment.FragmentListener,LoginFragment.LoginListener, QaFragment.QaListener, FunctionFragment.FunctionListener ,
        KnowledgeCheckFragment.KnowledgeCheckListener, RegisterFragment.RegisterListener, SearchFragment.FragmentListener, HomeFragment.FragmentListener, SearchResultListFragment.DetailListener,
        BrowsingHistoryListFragment.DataBaseListener, DetailFragment.detailListener{

    //组件
    private BottomNavigationView mBottomNavigationView;
    //Chip Group
    private String id;

    private List<Fragment> mFragments;
    private FragmentTransaction transaction;
    private FragmentManager mSupportFragmentManager;
    private Fragment currentFragment = null;

    private final String loginUrl = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
    private final String qaUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
    private final String searchPointUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";
    private final String detailUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName";

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private boolean firstInit = true;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private UserDatabaseHelper userDatabaseHelper;
    private SQLiteDatabase db;

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

        // 初始化fragments列表
        initFragments();

        // 设置底部导航栏监听器
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 获取fragment管理器
        mSupportFragmentManager = getSupportFragmentManager();

        switchFragments(0);
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                switchToHome();
                return true;
            case R.id.navigation_query:
                switchToQa();
                return true;
            case R.id.navigation_functions:
                switchToFunction();
                return true;
            case R.id.navigation_me:
                switchToMe();
                return true;
        }
        return false;
    };

    private void initElement(){
        mBottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
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
        mFragments.add(DetailFragment.newInstance("李白","chinese"));
        mFragments.add(BrowsingHistoryListFragment.newInstance());
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(QuizFragment.newInstance());
    }

    private void switchFragments(int FragmentId) {
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = mFragments.get(FragmentId);
        transaction.addToBackStack(null).replace(R.id.main_frame_layout, targetFragment).commit();
        currentFragment = targetFragment;
    }

    public void switchToHome() {switchFragments(0);}
    public void switchToQa() {switchFragments(1);}
    public void switchToFunction() {switchFragments(2);}
    public void switchToMe() {
        switchFragments(3);
        Log.d("MainActivity", "switchToMe");
        String username = sharedPreferences.getString("username", null);
        if (username != null && id != null) {
            EventBus.getDefault().postSticky(new LoginNotice(username));
            Log.d("MainActivity", "post new LoginNotice");
        } else {
            EventBus.getDefault().postSticky(new LogoutNotice());
            Log.d("MainActivity", "post new LogoutNotice");
        }
    }
    @Override
    public void switchToLogin(){
        switchFragments(4);
    }
    public void switchToRegister() { switchFragments(5); }
    public void switchToKnowledgeCheck(){
        switchFragments(6);
    }
    public void switchToSearch() {
        switchFragments(7);
    }
    public void switchToDetail() {
        switchFragments(8);
    }
    public void switchToBrowsingHistory() {
        switchFragments(9);
    }
    public void switchToFavourites() {
        switchFragments(10);
    }
    public void switchToReport() {
        switchFragments(11);
    }
    public void switchToQuiz() {
        switchFragments(12);
    }

    @Override
    public void logout() {
        id = null;
        sharedPreferencesEditor.remove("username");
        sharedPreferencesEditor.remove("password");
        sharedPreferencesEditor.remove("id");
        db.close();
        userDatabaseHelper = null;
        switchToMe();
    }

    @Override
    public void onBackPressed() {
        System.out.println("MainActivity:onBackPressed");
        if (mFragments.size() >= 1) {
            try {
                mSupportFragmentManager.popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void search(String subject, String searchKey) {
        if (!searchKey.equals("") && subject != null && id != null) {
            String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList"
                    + "?course=" + subject
                    + "&searchKey=" + searchKey
                    + "&id=" + id;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String response = OkHttp.get(url);
                        JSONObject jsonObject = new JSONObject(response);
                        String response_code = jsonObject.getString("code");
                        String message = jsonObject.getString("msg");
                        if (response_code.equals("0")) {
                            EventBus.getDefault().post(new MessageEvent(response));
                            ContentValues values = new ContentValues();
                            values.put("COURSE", subject);
                            values.put("SEARCHKEY", searchKey);
                            values.put("TIME", df.format(new Date()));
                            db.insert("SEARCHHISTORY", null, values);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "搜索成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else if (searchKey.equals("")) {
            Toast.makeText(MainActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
        } else if (id == null) {
            Toast.makeText(MainActivity.this, "您还没有登录，请先登录", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "请选择学科", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(String username, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://47.93.101.225:8080/login";
                String json = "{\n" +
                        " \"account\":\"" + username + "\",\n" +
                        " \"password\":\"" + password + "\"\n" +
                        "}";
                try {
                    String response = OkHttp.post(url, json);
                    try{
                        sharedPreferencesEditor.putString("username", username);
                        sharedPreferencesEditor.putString("password", password);
                        sharedPreferencesEditor.apply();
                    } catch(Exception e){
                        e.printStackTrace();
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
                            Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
        getId();
        switchToMe();
    }

    public void register(String username, String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://47.93.101.225:8080/register";
                String json = "{\n" +
                        " \"account\":\"" + username + "\",\n" +
                        " \"password\":\"" + password + "\"\n" +
                        "}";
                try {
                    String response = OkHttp.post(url, json);
                    JSONObject jsonObject = new JSONObject(response);
                    String response_code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (response_code.equals("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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
        switchToLogin();
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
                String username = sharedPreferences.getString("username", null);
                String password = sharedPreferences.getString("password", null);
                if (username != null && password != null) {
                    String json = "{\n" +
                            " \"phone\":\"" + "15272961269" + "\",\n" +
                            " \"password\":\"" + "lcs84615" + "\"\n" +
                            "}";
                    try {
                        String response = OkHttp.post(url, json);
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        try{
                            id = jsonObject.getString("id");
                            sharedPreferencesEditor.putString("id", id);
                            sharedPreferencesEditor.apply();

                            userDatabaseHelper = new UserDatabaseHelper(MainActivity.this, username + ".db");
                            db = userDatabaseHelper.getWritableDatabase();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "获取id成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch(Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "连接服务器失败，请重新打开APP!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "网络连接失败,请重新打开APP", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        }
                    });
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

    @Override
    public void getDetail(String entity_name, String course){
        if (entity_name == null | course == null) {
            Toast.makeText(MainActivity.this, "学科和实体名不得为空", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = detailUrl
                        + "?course=" + course
                        + "&name=" + entity_name
                        + "&id="+id;
                try {
                    String response = OkHttp.get(url);
                    JSONObject jsonObject = new JSONObject(response);
                    String response_code = jsonObject.getString("code");
                    if (response_code.equals("0")) {
                        ContentValues values = new ContentValues();
                        values.put("COURSE", course);
                        values.put("NAME", entity_name);
                        values.put("TIME", df.format(new Date()));
                        db.insert("BROWSINGHISTORY", null, values);
                        EventBus.getDefault().postSticky(new MessageEvent(response));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchToDetail();
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "获取内容失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public ArrayList<BrowsingHistory> getBrowsingHistory() {
        String username = sharedPreferences.getString("username", null);
        ArrayList<BrowsingHistory> browsingHistoryArrayList = null;
        if (id != null && username != null) {
            Cursor cursor = db.query("BROWSINGHISTORY", null, null, null, null, null, "ID DESC");
            if (cursor.moveToFirst()) {
                browsingHistoryArrayList = new ArrayList<BrowsingHistory>();
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndex("ID"));
                    String course = cursor.getString(cursor.getColumnIndex("COURSE"));
                    String name = cursor.getString(cursor.getColumnIndex("NAME"));
                    String time = cursor.getString(cursor.getColumnIndex("TIME"));
                    browsingHistoryArrayList.add(BrowsingHistory.newInstance(id, course, name, time));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            Toast.makeText(MainActivity.this, "请先登录，再查看浏览历史记录", Toast.LENGTH_SHORT).show();
        }
        return browsingHistoryArrayList;
    }
}