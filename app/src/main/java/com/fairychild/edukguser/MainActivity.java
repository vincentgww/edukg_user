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
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fairychild.edukguser.datastructure.BrowsingHistory;
import com.fairychild.edukguser.datastructure.LoginNotice;
import com.fairychild.edukguser.datastructure.LogoutNotice;
import com.fairychild.edukguser.datastructure.Question;
import com.fairychild.edukguser.fragment.BrowsingHistoryListFragment;
import com.fairychild.edukguser.fragment.FunctionFragment;
import com.fairychild.edukguser.fragment.KnowledgeCheckFragment;
import com.fairychild.edukguser.fragment.MeFragment;
import com.fairychild.edukguser.fragment.QaFragment;
import com.fairychild.edukguser.fragment.HomeFragment;
import com.fairychild.edukguser.fragment.LoginFragment;
import com.fairychild.edukguser.fragment.QuizFragment;
import com.fairychild.edukguser.fragment.RegisterFragment;

import com.fairychild.edukguser.fragment.SearchFragment;
import com.fairychild.edukguser.fragment.SubItemAdapter;
import com.fairychild.edukguser.fragment.detailFragment;
import com.fairychild.edukguser.sql.UserDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MeFragment.FragmentListener,
        LoginFragment.LoginListener,
        QaFragment.QaListener,
        FunctionFragment.FunctionListener,
        KnowledgeCheckFragment.KnowledgeCheckListener,
        RegisterFragment.RegisterListener,
        SearchFragment.FragmentListener,
        HomeFragment.FragmentListener,
        detailFragment.detailListener,
        QuizFragment.quizListener,
        SubItemAdapter.SubItemAdaptorListener,
        BrowsingHistoryListFragment.DataBaseListener{
    List<Fragment> mFragments;
    //组件
    private BottomNavigationView mBottomNavigationView;
    //适配器
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    //Chip Group
    private MenuItem menuItem;
    private String id;
    private FragmentTransaction transaction;
    private FragmentManager mSupportFragmentManager;
    private Fragment currentFragment;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final String loginUrl = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
    private final String qaUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
    private final String searchPointUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";
    private final String detailUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?";
    private final String quizUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?";
    private String str;
    private boolean firstInit = true;
    private List<Question> question_list = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private UserDatabaseHelper userDatabaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initElement();

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        getId();

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
        }
    };

    private void initElement(){
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.activity_main_bottom_navigation_view);
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
        mFragments.add(BrowsingHistoryListFragment.newInstance());
        mFragments.add(SearchFragment.newInstance());
    }

    public void switchToHome() {
        switchFragments(0);
    }
    public void switchToQa() {
        switchFragments(1);
    }
    public void switchToFunction() {
        switchFragments(2);
    }
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
    public void switchToRegister() {switchFragments(5);}
    public void switchToKnowledgeCheck(){
        switchFragments(6);
    }
    public void switchToBrowsingHistory() {
        switchFragments(7);
    }
    public void switchToSearch() {
        switchFragments(8);
    }
    public void switchToFavourites() {
        switchFragments(9);
    }
    public void switchToReport() {
        switchFragments(10);
    }

    private void switchFragments(int FragmentId) {
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = mFragments.get(FragmentId);
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.frameLayout,targetFragment);
        }
        if(!firstInit)
            transaction.hide(currentFragment);
        firstInit = false;
        transaction.show(targetFragment);
        transaction.commit();
        currentFragment = targetFragment;
    }

    public void show_detail_fragment(String label,String course){
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = detailFragment.newInstance(label,course,mFragments.size());
        mFragments.add(targetFragment);
        transaction.add(R.id.frameLayout,targetFragment);
        transaction.hide(currentFragment);
        transaction.show(targetFragment);
        transaction.commit();
        currentFragment = targetFragment;
    }

    public void delete_fragment(int idx){
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = mFragments.get(idx);
        transaction.remove(targetFragment);
        mFragments.remove(idx);
    }

    public void delete_quiz(int idx){
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = mFragments.get(idx);
        transaction.remove(targetFragment);
        mFragments.remove(idx);
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
    public String getIdFromSP() {
        return sharedPreferences.getString("id", null);
    }

    @Override
    public String getUsernameFromSP() {
        return sharedPreferences.getString("username", null);
    }

    @Override
    public void logout() {
        Log.d("MainActivity", "logout");
        id = null;
        sharedPreferencesEditor.remove("username");
        sharedPreferencesEditor.remove("password");
        sharedPreferencesEditor.remove("id");
        db.close();
        userDatabaseHelper = null;
        switchToMe();
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
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    try{
                        sharedPreferencesEditor.putString("username", username);
                        sharedPreferencesEditor.putString("password", password);
                        sharedPreferencesEditor.apply();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch(Exception e){
                        e.printStackTrace();
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
                            Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
        getId();
    }

    public void register(String username, String password){
        new Thread(() -> {
            String url="http://47.93.101.225:8080/register";
            String json = "{\n" +
                    " \"account\":\"" + username + "\",\n" +
                    " \"password\":\"" + password + "\"\n" +
                    "}";
            try {
                String response = OkHttp.post(url, json);
                System.out.println(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "注册成功，请登录！", Toast.LENGTH_SHORT).show();
                        switchToLogin();
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
        return str;
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
                }
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
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

    public void get_detail(String entity_name, String course){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Cursor cursor = db.query("DETAIL", new String[]{"CONTENT"},
                            "COURSE=? AND NAME=?", new String[]{course, entity_name},
                            null, null, null);
                    String response = null;
                    if (cursor.moveToFirst()) {
                        response = cursor.getString(cursor.getColumnIndex("CONTENT"));
                    } else {
                        String url = detailUrl
                                + "course=" + course
                                + "&name=" + entity_name
                                + "&id=" + id;
                        response = OkHttp.get(url);
                    }
                    EventBus.getDefault().post(new MessageEvent(response));
                    ContentValues values = new ContentValues();
                    values.put("COURSE", course);
                    values.put("NAME", entity_name);
                    values.put("TIME", df.format(new Date()));
                    db.insert("BROWSINGHISTORY", null, values);
                    values.clear();
                    values.put("COURSE", course);
                    values.put("NAME", entity_name);
                    values.put("CONTENT", response);
                    db.insertWithOnConflict("DETAIL", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    values.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "获取详情成功！", Toast.LENGTH_SHORT).show();
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

    public void related_quiz(String name){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = quizUrl
                        + "uriName=" + name
                        + "&id=" + id;
                try {
                    String response = OkHttp.get(url);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transaction = mSupportFragmentManager.beginTransaction();
                            question_list = new ArrayList<>();
                            handle_quiz(response);
                            Fragment targetFragment = QuizFragment.newInstance(name,mFragments.size(),question_list);
                            mFragments.add(targetFragment);
                            transaction.add(R.id.frameLayout,targetFragment);
                            transaction.hide(currentFragment);
                            transaction.show(targetFragment);
                            transaction.commit();
                            currentFragment = targetFragment;
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

    private void handle_quiz (String response){
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = new JSONArray(obj.getString("data"));
            for(int i=0;i<arr.length();i++){
                obj = arr.getJSONObject(i);
                System.out.println(obj);
                String raw = obj.getString("qBody");
                String ans = obj.getString("qAnswer");
                int correct=-1;
                switch (ans){
                    case "A":
                        correct = 0;
                        break;
                    case "B":
                        correct = 1;
                        break;
                    case "C":
                        correct = 2;
                        break;
                    case "D":
                        correct = 3;
                }
                if(raw.length()<1000) {
                    String[] blocks = raw.split("[A-D]\\.");
                    Question q = new Question(blocks[0], blocks[1], blocks[2], blocks[3], blocks[4], correct);
                    question_list.add(q);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
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