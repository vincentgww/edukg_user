package com.fairychild.edukguser;
import androidx.fragment.app.FragmentManager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fairychild.edukguser.datastructure.BrowsingHistory;
import com.fairychild.edukguser.datastructure.BrowsingHistoryListFragmentRefreshNotice;
import com.fairychild.edukguser.datastructure.DetailMessageEvent;
import com.fairychild.edukguser.datastructure.Favourite;
import com.fairychild.edukguser.datastructure.FavouritesListFragmentRefreshNotice;
import com.fairychild.edukguser.datastructure.LocalCache;
import com.fairychild.edukguser.datastructure.LocalCacheListFragmentRefreshNotice;
import com.fairychild.edukguser.datastructure.LoginNotice;
import com.fairychild.edukguser.datastructure.LogoutNotice;
import com.fairychild.edukguser.Activity.WebShareActivity;
import com.fairychild.edukguser.datastructure.Question;
import com.fairychild.edukguser.datastructure.outlineItem;
import com.fairychild.edukguser.fragment.BrowsingHistoryListFragment;
import com.fairychild.edukguser.fragment.FavouritesListFragment;
import com.fairychild.edukguser.fragment.FunctionFragment;
import com.fairychild.edukguser.fragment.KnowledgeCheckFragment;
import com.fairychild.edukguser.fragment.LocalCacheListFragment;
import com.fairychild.edukguser.fragment.MeFragment;
import com.fairychild.edukguser.fragment.OutlineFragment;
import com.fairychild.edukguser.fragment.QaFragment;
import com.fairychild.edukguser.fragment.HomeFragment;
import com.fairychild.edukguser.fragment.LoginFragment;
import com.fairychild.edukguser.fragment.QuestionFragment;
import com.fairychild.edukguser.fragment.QuizFragment;
import com.fairychild.edukguser.fragment.RegisterFragment;

import com.fairychild.edukguser.fragment.SearchFragment;
import com.fairychild.edukguser.fragment.SearchResultListAdapter;
import com.fairychild.edukguser.fragment.SearchResultListFragment;
import com.fairychild.edukguser.fragment.SubItemAdapter;
import com.fairychild.edukguser.fragment.detailFragment;
import com.fairychild.edukguser.sql.UserDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

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
        BrowsingHistoryListFragment.DataBaseListener,
        FavouritesListFragment.DataBaseListener,
        SearchResultListFragment.DetailListener,
        LocalCacheListFragment.myListener,
        SearchResultListAdapter.myListener,
        QuestionAdapter.myListener,
        OutlineFragment.outlineListener{
    List<Fragment> mFragments;
    //组件
    private BottomNavigationView mBottomNavigationView;
    //适配器
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    //Chip Group
    private MenuItem menuItem;
    private String id;
    private String uid;
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
    private final String outlineUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/relatedsubject";
    private final String getHistoryUrl = "http://47.93.101.225:8080/user/history/";
    private final String setHistoryUrl = "http://47.93.101.225:8080/history";
    private final String getFavouritesUrl = "http://47.93.101.225:8080/user/collect/";
    private final String addFavouritesUrl = "http://47.93.101.225:8080/collect";
    private final String removeFavouritesUrl = "http://47.93.101.225:8080/removecollect";
    private final String checkFavouritesUrl = "http://47.93.101.225:8080/checkcollect";
    private final String getQuestionsUrl = "http://47.93.101.225:8080/user/question/";
    private final String addQuestionsUrl = "http://47.93.101.225:8080/question";
    private String str;
    private boolean firstInit = true;
    private List<Question> question_list = new ArrayList<>();
    private List<outlineItem> outline_list = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private UserDatabaseHelper userDatabaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        getId();
        initElement();

        initFragments();
        WbSdk.install(this,new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE));


        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSupportFragmentManager = getSupportFragmentManager();
        mBottomNavigationView.setSelectedItemId(R.id.navigation_me);

        id = sharedPreferences.getString("id", null);
        uid = sharedPreferences.getString("uid", null);
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
        mFragments.add(FavouritesListFragment.newInstance());
        mFragments.add(LocalCacheListFragment.newInstance());
    }

    public void switchToHome() {
        Log.d("MainActivity", "switchToHome");
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
        if (username != null && sharedPreferences.getString("id", null) != null && db != null) {
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
    public void switchToBrowsingHistory() throws InterruptedException {
        BrowsingHistoryListFragment browsingHistoryListFragment = (BrowsingHistoryListFragment) mFragments.get(7);
        ArrayList<BrowsingHistory> browsingHistoryArrayList = getBrowsingHistory();
        browsingHistoryListFragment.setmData(browsingHistoryArrayList);
        EventBus.getDefault().postSticky(new BrowsingHistoryListFragmentRefreshNotice());
        Log.d("switchToBrowsingHistory", browsingHistoryArrayList.toString());
        switchFragments(7);
    }
    public void switchToSearch() {
        switchFragments(8);
    }
    public void switchToFavourites() throws InterruptedException {
        FavouritesListFragment favouritesListFragment = (FavouritesListFragment) mFragments.get(9);
        ArrayList<Favourite> favouriteArrayList = getFavourites();
        favouritesListFragment.setmData(favouriteArrayList);
        EventBus.getDefault().postSticky(new FavouritesListFragmentRefreshNotice());
        Log.d("switchToFavourites", favouriteArrayList.toString());
        switchFragments(9);
    }

    @Override
    public void switchToLocalCache() {
        Log.d("MainActivity", "switchToLocalCache");
        LocalCacheListFragment localCacheListFragment = (LocalCacheListFragment) mFragments.get(10);
        ArrayList<LocalCache> localCacheArrayList = getLocalCacheList();
        if (localCacheArrayList != null) {
            localCacheListFragment.setmData(localCacheArrayList);
            EventBus.getDefault().postSticky(new LocalCacheListFragmentRefreshNotice());
            Log.d("switchToLocalCache", localCacheArrayList.toString());
            switchFragments(10);
        } else {
            Log.d("switchToLocalCache", "localCacheArrayList == null");
        }
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

    @Override
    public ArrayList<LocalCache> getLocalCacheList() {
        Log.d("MainActivity", "getLocalCacheList");
        String username = sharedPreferences.getString("username", null);
        id = sharedPreferences.getString("id", id);
        ArrayList<LocalCache> localCacheArrayList = null;
        if (username != null && id != null && db != null && db.isOpen()) {
            Cursor cursor = db.query("LOCALCACHE", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                localCacheArrayList = new ArrayList<LocalCache>();
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndex("ID"));
                    String course = cursor.getString(cursor.getColumnIndex("COURSE"));
                    String name = cursor.getString(cursor.getColumnIndex("NAME"));
                    String time = cursor.getString(cursor.getColumnIndex("TIME"));
                    localCacheArrayList.add(LocalCache.newInstance(id, course, name, time));
                } while (cursor.moveToNext());
            }
            cursor.close();
            if (localCacheArrayList != null) {
                localCacheArrayList.sort(new Comparator<LocalCache>() {
                    @Override
                    public int compare(LocalCache localCache, LocalCache t1) {
                        try {
                            Date localCacheTime =  df.parse(localCache.getTime());
                            Date t1Time = df.parse(t1.getTime());
                            if (t1Time != null && localCacheTime != null) {
                                return t1Time.compareTo(localCacheTime);
                            } else {
                                return Integer.compare(t1.getId(), localCache.getId());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Integer.compare(t1.getId(), localCache.getId());
                        }
                    }
                });
            }
//            Toast.makeText(MainActivity.this, "获取本地缓存成功", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("getLocalCacheList", username + " " + id + " " + (db == null));
            Toast.makeText(MainActivity.this, "请先登录，再查看本地缓存", Toast.LENGTH_SHORT).show();
        }
        return localCacheArrayList;
    }

    public void addLocalCacheList(String course, String name) {
        String username = sharedPreferences.getString("username", null);
        id = sharedPreferences.getString("id", id);
        if (username != null && id != null && db != null && db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("COURSE", course);
            values.put("NAME", name);
            values.put("TIME", df.format(new Date()));
            db.insertWithOnConflict("LOCALCACHE", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(MainActivity.this, "添加本地缓存成功", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "请先登录，再添加本地缓存", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void show_detail_fragment(String label, String course, int back_id){
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = detailFragment.newInstance(label,course,mFragments.size(), back_id);
        mFragments.add(targetFragment);
        transaction.add(R.id.frameLayout,targetFragment);
        transaction.hide(currentFragment);
        transaction.show(targetFragment);
        transaction.commit();
        currentFragment = targetFragment;
    }

    @Override
    public SQLiteDatabase getSQLiteDatabase() {
        if (userDatabaseHelper != null) {
            try {
                return userDatabaseHelper.getReadableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
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

    public void weibo_share(String item_title,String item_content){
        WbShareHandler shareHandler=new WbShareHandler(MainActivity.this);
        shareHandler.registerApp();
        Intent i=new Intent(MainActivity.this, WebShareActivity.class);
        i.putExtra(WebShareActivity.KEY_SHARE_TYPE,WebShareActivity.SHARE_CLIENT);
        i.putExtra("itemTitle",item_title);
        i.putExtra("itemContent",item_content);
        startActivity(i);
    }

    public void back_home(int back_id){
        switchFragments(back_id);
    }
    @Override
    public void onBackPressed() {
        switchToHome();
    }

    /*public Bitmap getBitmapFromURL(String src) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Log.e("src",src);

                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    //Log.e("Bitmap","returned");
                    return myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    //Log.e("Exception",e.getMessage());
                    return null;
                }
            }
        }).start();
    }*/

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
        uid = null;
        sharedPreferencesEditor.remove("username");
        sharedPreferencesEditor.remove("password");
        sharedPreferencesEditor.remove("id");
        sharedPreferencesEditor.remove("uid");
        sharedPreferencesEditor.apply();
        if (db != null) {
            db.close();
        }
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
                    String response = OkHttp.post(url, json, MainActivity.this);
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject dataObject = new JSONObject(data);
                    uid = dataObject.getString("id");
                    try{
                        sharedPreferencesEditor.putString("username", username);
                        sharedPreferencesEditor.putString("password", password);
                        sharedPreferencesEditor.putString("uid", uid);
                        sharedPreferencesEditor.apply();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                getId();
                            }
                        });
                    } catch(Exception e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "登录失败！请重试!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void register(String username, String password){
        new Thread(() -> {
            String url="http://47.93.101.225:8080/register";
            String json = "{\n" +
                    " \"account\":\"" + username + "\",\n" +
                    " \"password\":\"" + password + "\"\n" +
                    "}";
            try {
                String response = OkHttp.post(url, json, MainActivity.this);

                JSONObject responseObject = new JSONObject(response);
                String response_code = responseObject.getString("code");
                String response_message = responseObject.getString("message");

//                System.out.println(response);
                if (response_code.equals("0")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "注册成功，请登录！", Toast.LENGTH_SHORT).show();
                            switchToLogin();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, response_message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "注册失败！请重试！", Toast.LENGTH_SHORT).show();
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
                    String response = OkHttp.post(url, json, MainActivity.this);
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
                        String response = OkHttp.post(url, json, MainActivity.this);
//                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        try{
                            id = jsonObject.getString("id");
                            Log.d("id", id);
                            sharedPreferencesEditor.putString("id", id);
                            sharedPreferencesEditor.apply();

                            userDatabaseHelper = new UserDatabaseHelper(MainActivity.this, username + ".db");
                            db = userDatabaseHelper.getWritableDatabase();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(MainActivity.this, "获取网络token成功！", Toast.LENGTH_SHORT).show();
                                    switchToMe();
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
                                Log.d("MainActivity", "网络连接失败,请重新打开APP");
                                Toast.makeText(MainActivity.this, "网络连接失败,请重新打开APP", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "没有检测到用户名或密码，请重新登录！", Toast.LENGTH_SHORT).show();
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
                    String response = OkHttp.post(url, json, MainActivity.this);
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
                        Log.d("getDetailFromDataBase", response);
                    } else {
                        String url = detailUrl
                                + "course=" + course
                                + "&name=" + entity_name
                                + "&id=" + id;
                        response = OkHttp.get(url, MainActivity.this);
                        Log.d("getDetailFromInternet", response);
                    }
                    setBrowsingHistory(course, entity_name);
                    EventBus.getDefault().postSticky(new DetailMessageEvent(response));
                    ContentValues values = new ContentValues();
                    values.put("COURSE", course);
                    values.put("NAME", entity_name);
                    values.put("CONTENT", response);
                    db.insertWithOnConflict("DETAIL", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    addLocalCacheList(course, entity_name);
                    values.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(MainActivity.this, "获取详情成功！", Toast.LENGTH_SHORT).show();
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

    public void go_back(int back_id){
        switchFragments(back_id);
    }

    public void related_question(String name, int idx){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = quizUrl
                        + "uriName=" + name
                        + "&id=" + id;
                try {
                    String response = OkHttp.get(url, MainActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transaction = mSupportFragmentManager.beginTransaction();
                            question_list = handle_quiz(response, name);
                            if(question_list.isEmpty()){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Toast toast = Toast.makeText(MainActivity.this, "无相关试题！", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                            else {
                                Fragment targetFragment = QuestionFragment.newInstance(name, mFragments.size(), question_list, idx);
                                mFragments.add(targetFragment);
                                transaction.add(R.id.frameLayout, targetFragment);
                                transaction.hide(currentFragment);
                                transaction.show(targetFragment);
                                transaction.commit();
                                currentFragment = targetFragment;
                            }
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

    @Override
    public ArrayList<Favourite> getFavourites() throws InterruptedException {
        id = sharedPreferences.getString("id", null);
        uid = sharedPreferences.getString("uid", null);
        ArrayList<Favourite> favouritesArrayList = new ArrayList<Favourite>();
        if (id != null && uid != null) {
            String url = getFavouritesUrl
                    + uid;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String response = OkHttp.get(url, MainActivity.this);
                        JSONObject responseObject = new JSONObject(response);
                        String response_code = responseObject.getString("code");
                        String response_message = responseObject.getString("message");
                        if (response_code.equals("0")) {
                            String data = responseObject.getString("data");
                            JSONObject dataObject = new JSONObject(data);
                            String collects = dataObject.getString("collects");
                            JSONArray collectsArray = new JSONArray(collects);
                            for (int i = 0; i < collectsArray.length(); i++) {
                                JSONObject favourite = collectsArray.getJSONObject(i);
                                String course = favourite.getString("course");
                                String name = favourite.getString("label");
                                favouritesArrayList.add(Favourite.newInstance(i, course, name));
                            }
                            favouritesArrayList.sort(new Comparator<Favourite>() {
                                @Override
                                public int compare(Favourite favourite, Favourite t1) {
                                    return Integer.compare(t1.getId(), favourite.getId());
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, response_message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "获取收藏夹信息失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            thread.start();
            thread.join();
        } else {
            Toast.makeText(MainActivity.this, "请先登录，再查看收藏夹", Toast.LENGTH_SHORT).show();
        }
        return favouritesArrayList;
    }

    @Override
    public void addFavourites(String course, String name) {
        id = sharedPreferences.getString("id", null);
        uid = sharedPreferences.getString("uid", null);
        if (id != null && uid != null) {
            String url = addFavouritesUrl;
            String json = "{" +
                    "    \"course\":\"" + course + "\",\n" +
                    "    \"label\":\"" + name + "\",\n" +
                    "    \"uid\":" + uid + "" +
                    "}";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String response = OkHttp.post(url, json, MainActivity.this);
                        JSONObject responseObject = new JSONObject(response);
                        String response_code = responseObject.getString("code");
                        String response_message = responseObject.getString("message");
                        if (response_code.equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(MainActivity.this, "添加收藏夹成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, response_message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "添加收藏夹失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "您还没有登录，请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void removeFavourites(String course, String name) {
        id = sharedPreferences.getString("id", null);
        uid = sharedPreferences.getString("uid", null);
        if (id != null && uid != null) {
            String url = removeFavouritesUrl;
            String json = "{" +
                    "    \"course\":\"" + course + "\",\n" +
                    "    \"label\":\"" + name + "\",\n" +
                    "    \"uid\":" + uid + "" +
                    "}";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String response = OkHttp.post(url, json, MainActivity.this);
                        JSONObject responseObject = new JSONObject(response);
                        String response_code = responseObject.getString("code");
                        String response_message = responseObject.getString("message");
                        if (response_code.equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, response_message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "您还没有登录，请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isAddedToFavourites(String course, String name) {
        id = sharedPreferences.getString("id", null);
        uid = sharedPreferences.getString("uid", null);
        if (id != null && uid != null) {
            String url = checkFavouritesUrl;
            String json = "{" +
                    "    \"course\":\"" + course + "\",\n" +
                    "    \"label\":\"" + name + "\",\n" +
                    "    \"uid\":" + uid +
                    "}";
            Callable<Boolean> callable = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    try {
                        String response = OkHttp.post(url, json, MainActivity.this);
                        JSONObject responseObject = new JSONObject(response);
                        String response_code = responseObject.getString("code");
                        String response_message = responseObject.getString("message");
                        if (response_code.equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "已存在于收藏夹", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return true;
                        } else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(MainActivity.this, response_message, Toast.LENGTH_SHORT).show();
//                                }
//                            });
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "查询是否存在于收藏夹失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return false;
                    }
                }
            };
            FutureTask<Boolean> task = new FutureTask<Boolean>(callable);
            Thread thread = new Thread(task);
            thread.start();
            try {
                return task.get();
            } catch (Exception e) {
                return false;
            }
        } else {
            Toast.makeText(MainActivity.this, "您还没有登录，请先登录", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private ArrayList<Question> handle_quiz (String response, String label){
        //System.out.println(response);
        ArrayList<Question> questionArrayList = new ArrayList<Question>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = new JSONArray(obj.getString("data"));
            for(int i=0;i<arr.length();i++){
                obj = arr.getJSONObject(i);
//                System.out.println(obj);
                String raw = obj.getString("qBody");
                String ans = obj.getString("qAnswer");
                Integer id = obj.getInt("id");
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
                if(raw.length()<1000 && ans.length()==1) {
                    String[] blocks = raw.split("([A-D]\\.)|([A-D]．)");
                    // System.out.println(blocks[0]);
                    Question q = new Question(label, id, blocks[0], blocks[1], blocks[2], blocks[3], blocks[4], correct);
                    questionArrayList.add(q);
                    //System.out.println(q);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return questionArrayList;
    }

    @Override
    public ArrayList<BrowsingHistory> getBrowsingHistory() throws InterruptedException {
        String username = sharedPreferences.getString("username", null);
        ArrayList<BrowsingHistory> browsingHistoryArrayList = new ArrayList<BrowsingHistory>();
        if (id != null && username != null && uid != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = getHistoryUrl + uid;
                        String response = OkHttp.get(url, MainActivity.this);
                        JSONObject responseObject = new JSONObject(response);
                        String response_code = responseObject.getString("code");
                        if (response_code.equals("0")) {
                            String data = responseObject.getString("data");
                            JSONObject dataObject = new JSONObject(data);
                            String historys = dataObject.getString("historys");
                            JSONArray historyArray = new JSONArray(historys);
                            for (int i = 0; i < historyArray.length(); i++) {
                                JSONObject history = historyArray.getJSONObject(i);
                                String course = history.getString("course");
                                String name = history.getString("label");
                                String time = history.getString("time");
                                browsingHistoryArrayList.add(BrowsingHistory.newInstance(i, course, name, time));
                            }
                            browsingHistoryArrayList.sort(new Comparator<BrowsingHistory>() {
                                @Override
                                public int compare(BrowsingHistory browsingHistory, BrowsingHistory t1) {
                                    try {
                                        Date browsingHistoryTime =  df.parse(browsingHistory.getTime());
                                        Date t1Time = df.parse(t1.getTime());
                                        if (t1Time != null && browsingHistoryTime != null) {
                                            return t1Time.compareTo(browsingHistoryTime);
                                        } else {
                                            return Integer.compare(t1.getId(), browsingHistory.getId());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return Integer.compare(t1.getId(), browsingHistory.getId());
                                    }
                                }
                            });
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(MainActivity.this, "获取浏览记录成功！", Toast.LENGTH_SHORT).show();
                                    Log.d("BrowsingHistory", browsingHistoryArrayList.toString());
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("getBrowsingHistory", url);
                                    Log.d("getBrowsingHistory", response);
                                    Toast.makeText(MainActivity.this, "获取浏览记录失败！请重新尝试！", Toast.LENGTH_SHORT).show();
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
            });
            thread.start();
            thread.join();
        } else {
            Toast.makeText(MainActivity.this, "请先登录，再查看浏览历史记录", Toast.LENGTH_SHORT).show();
        }
        return browsingHistoryArrayList;
    }

    public void setBrowsingHistory(String course, String name) {
        String username = sharedPreferences.getString("username", null);
        if (id != null && username != null && uid != null) {
            String url = setHistoryUrl;
            String json = "{\n" +
                    "    \"course\":\"" + course + "\",\n" +
                    "    \"label\":\"" + name + "\",\n" +
                    "    \"time\":\"" + df.format(new Date()) + "\",\n" +
                    "    \"uid\":\"" + uid +
                    "\"}";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String response = OkHttp.post(url, json, MainActivity.this);
                        Log.d("setBrowsingHistory", response);
                        JSONObject responseObject = new JSONObject(response);
                        String response_code = responseObject.getString("code");
                        if (response_code.equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(MainActivity.this, "添加历史记录成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "添加历史记录失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("setBrowsingHistory", url + json);
                                Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("setBrowsingHistory", id);
                    Log.d("setBrowsingHistory", username);
                    Log.d("setBrowsingHistory", uid);
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferencesEditor.remove("id");
        id = null;
        userDatabaseHelper = null;
    }

    public void show_quiz(String label,String course){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = quizUrl
                        + "uriName=" + label
                        + "&id=" + id;
                try {
                    String response = OkHttp.get(url, MainActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transaction = mSupportFragmentManager.beginTransaction();
                            question_list = handle_quiz(response, label);
                            if(question_list.isEmpty()){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Toast toast = Toast.makeText(MainActivity.this, "无相关试题！", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                            else {
                                Fragment targetFragment = QuizFragment.newInstance(label, mFragments.size(), question_list, 2);
                                mFragments.add(targetFragment);
                                transaction.add(R.id.frameLayout, targetFragment);
                                transaction.hide(currentFragment);
                                transaction.show(targetFragment);
                                transaction.commit();
                                currentFragment = targetFragment;
                            }
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

    public ArrayList<Question> getQuestionArrayList() {
        String username = sharedPreferences.getString("username", null);
        id = sharedPreferences.getString("id", null);
        uid = sharedPreferences.getString("uid", null);
        ArrayList<Question> questionArrayList = new ArrayList<Question>();
        if (username != null && id != null && uid != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = getQuestionsUrl
                            + uid;
                    try {
                        String response = OkHttp.get(url, MainActivity.this);
                        JSONObject responseObject = new JSONObject(response);
                        String response_code = responseObject.getString("code");
                        String response_message = responseObject.getString("message");
                        if (response_code.equals("0")) {
                            String data = responseObject.getString("data");
                            JSONObject dataObject = new JSONObject(data);
                            String questions = dataObject.getString("questions");
                            JSONArray questionArray = new JSONArray(questions);
                            for (int i = 0; i < questionArray.length(); i++) {
                                try{
                                    JSONObject history = questionArray.getJSONObject(i);
                                    String label = history.getString("label");
                                    int id = history.getInt("id");
                                    String title = history.getString("title");
                                    String choiceA = history.getString("choiceA");
                                    String choiceB = history.getString("choiceB");
                                    String choiceC = history.getString("choiceC");
                                    String choiceD = history.getString("choiceD");
                                    int correct = history.getInt("correct");
                                    int usr_ans = history.getInt("usr_ans");
                                    Question question = new Question(label, id, title,
                                            choiceA, choiceB, choiceC, choiceD, correct);
                                    question.set_usr_ans(usr_ans);
                                    questionArrayList.add(question);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(MainActivity.this, "获取做题记录成功！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, response_message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                System.out.println("获取做题记录失败");
                                Toast.makeText(MainActivity.this, "获取做题记录失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "请先登录，再查看做题记录", Toast.LENGTH_SHORT).show();
        }
        return questionArrayList;
    }

    public ArrayList<Question> getRelatedQuestions(String label) throws InterruptedException {

        final ArrayList<Question> questionArrayList = new ArrayList<Question>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = quizUrl
                        + "uriName=" + label
                        + "&id=" + id;
                try {
                    String response = OkHttp.get(url, MainActivity.this);
                    JSONObject responseObject = new JSONObject(response);
                    String response_code = responseObject.getString("code");
                    String response_message = responseObject.getString("msg");
                    if (response_code.equals("0")) {
                        questionArrayList.addAll(handle_quiz(response, label));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, response_message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            System.out.println("获取相关试题列表失败");
                            Toast.makeText(MainActivity.this, "获取相关试题列表失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        thread.start();
        thread.join();
        return questionArrayList;
    }

    @Override
    public void show_exam() throws InterruptedException {

        String username = sharedPreferences.getString("username", null);
        id = sharedPreferences.getString("id", null);

        if (username != null && id != null) {
            ArrayList<LocalCache> localCacheArrayList;
            ArrayList<Question> questionArrayList;
            ArrayList<Question> examQuestions = new ArrayList<Question>();

            Callable<ArrayList<LocalCache>> callable = new Callable<ArrayList<LocalCache>>() {
                @Override
                public ArrayList<LocalCache> call() {
                    return getLocalCacheList();
                }
            };
            FutureTask<ArrayList<LocalCache>> task = new FutureTask<ArrayList<LocalCache>>(callable);
            Thread localCacheThread = new Thread(task);
            localCacheThread.start();

            questionArrayList = getQuestionArrayList();

            localCacheThread.join();
            try {
                localCacheArrayList = task.get();
            } catch (Exception e) {
                localCacheArrayList = null;
            }

            if (localCacheArrayList == null || localCacheArrayList.size() == 0) {
                if (questionArrayList.size() < 10) {
                    Toast.makeText(MainActivity.this, "您的浏览历史和做题记录太少，无法生成推荐试题！", Toast.LENGTH_SHORT).show();
                } else {
                    Collections.shuffle(questionArrayList);
                    questionArrayList.sort(new Comparator<Question>() {
                        @Override
                        public int compare(Question question, Question t1) {
                            if (question.ansIsRight() && !t1.ansIsRight()) {
                                return 1;
                            } else if (!question.ansIsRight() && t1.ansIsRight()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    for (int i = 0; i < 10; i++) {
                        examQuestions.add(questionArrayList.get(i));
                    }
//                    Toast.makeText(MainActivity.this, "生成推荐试题成功！", Toast.LENGTH_SHORT).show();
                    Fragment targetFragment = QuizFragment.newInstance(null, mFragments.size(), examQuestions, 2);
                    mFragments.add(targetFragment);
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    transaction1.add(R.id.frameLayout, targetFragment);
                    transaction1.hide(currentFragment);
                    transaction1.show(targetFragment);
                    transaction1.commit();
                    currentFragment = targetFragment;
                }
            } else {
//                System.out.println(localCacheArrayList);
//                double startTime = System.currentTimeMillis();
                ArrayList<Thread> threadArrayList = new ArrayList<Thread>();
                int count = 0;
                for (LocalCache localCache : localCacheArrayList) {
                    if (count > 20) {
                        break;
                    }
                    count++;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = quizUrl
                                    + "uriName=" + localCache.getName()
                                    + "&id=" + id;
                            try {
                                String response = OkHttp.get(url, MainActivity.this);
                                JSONObject responseObject = new JSONObject(response);
                                String response_code = responseObject.getString("code");
                                String response_message = responseObject.getString("msg");
                                if (response_code.equals("0")) {
                                    ArrayList<Question> tmpQuestionArrayList = handle_quiz(response, localCache.getName());
                                    synchronized (examQuestions) {
                                        examQuestions.addAll(tmpQuestionArrayList);
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, response_message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "获取相关试题列表失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    threadArrayList.add(thread);
                    thread.start();
                }
//                System.out.println("count="+count);
                for (Thread thread : threadArrayList) {
                    thread.join();
                }

                Collections.shuffle(examQuestions);

                examQuestions.sort(new Comparator<Question>() {
                    @Override
                    public int compare(Question question, Question t1) {
                        if (question.ansIsRight() && !t1.ansIsRight()) {
                            return 1;
                        } else if (!question.ansIsRight() && t1.ansIsRight()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

//                double endTime = System.currentTimeMillis();
//                System.out.println("size:" + examQuestions.size());
//                System.out.println("duration: " + ((endTime - startTime) / 1000.0));
                if (examQuestions.size() < 10) {
                    Toast.makeText(MainActivity.this, "您的浏览历史和做题记录太少，无法生成推荐试题！", Toast.LENGTH_SHORT).show();
                } else {
                    examQuestions.sort(new Comparator<Question>() {
                        @Override
                        public int compare(Question question, Question t1) {
                            int questionStatus = 1;
                            int t1Status = 1;

                            if (questionArrayList != null && questionArrayList.contains(question)) {
                                boolean right = questionArrayList.get(questionArrayList.indexOf(question)).ansIsRight();
                                questionStatus = right ? 2 : 0;
                            }

                            if (questionArrayList != null && questionArrayList.contains(t1)) {
                                boolean right = questionArrayList.get(questionArrayList.indexOf(t1)).ansIsRight();
                                t1Status = right ? 2 : 0;
                            }

                            return Integer.compare(questionStatus, t1Status);
                        }
                    });
//                    System.out.println(examQuestions.subList(0, 10));
//                    Toast.makeText(MainActivity.this, "生成推荐试题成功！", Toast.LENGTH_SHORT).show();
                    Fragment targetFragment = QuizFragment.newInstance(null, mFragments.size(), examQuestions.subList(0, 10), 2);
                    mFragments.add(targetFragment);
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    transaction1.add(R.id.frameLayout, targetFragment);
                    transaction1.hide(currentFragment);
                    transaction1.show(targetFragment);
                    transaction1.commit();
                    currentFragment = targetFragment;
                }
            }
        }
    }

    public void addQuestion(Question question) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = addQuestionsUrl;
                try {
                    String json = "{\n" +
                            "    \"label\":\"" + question.getLabel() +  "\",\n" +
                            "    \"id\":" + question.getId() + ",\n" +
                            "    \"title\":\"" + question.getTitle() + "\",\n" +
                            "    \"choiceA\":\"" + question.getChoiceA() + "\",\n" +
                            "    \"choiceB\":\"" + question.getChoiceB() + "\",\n" +
                            "    \"choiceC\":\"" + question.getChoiceC() + "\",\n" +
                            "    \"choiceD\":\"" + question.getChoiceD() + "\",\n" +
                            "    \"correct\":" + question.getAns() + ",\n" +
                            "    \"usr_ans\":" + question.get_usr_ans() + ",\n" +
                            "    \"uid\":" + uid + "\n" +
                            "}";
                    String response = OkHttp.post(url, json, MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "上传做题记录失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void switchToOutline(String course,String label){
        //System.out.println(course);
        //System.out.println(label);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = outlineUrl;
                String json = "{\n" +
                        " \"subjectName\":\"" + label + "\",\n" +
                        " \"course\":\"" + course + "\",\n" +
                        " \"id\":\"" + id + "\"\n" +
                        "}";
                try {
                    String response = OkHttp.post(url, json,MainActivity.this);
                    //EventBus.getDefault().post(new MessageEvent(response));
                    handle_outline(response,label);
                    if(outline_list.isEmpty()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this, "无相关知识点！", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                    else {
                        Fragment targetFragment = OutlineFragment.newInstance(label, mFragments.size(), outline_list, 2);
                        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
                        mFragments.add(targetFragment);
                        transaction.add(R.id.frameLayout, targetFragment);
                        transaction.hide(currentFragment);
                        transaction.show(targetFragment);
                        transaction.commit();
                        currentFragment = targetFragment;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handle_outline(String response,String label){
        try {
            outline_list = new ArrayList<>();
            JSONObject obj = new JSONObject(response);
            JSONArray arr = new JSONArray(obj.getString("data"));
            List<outlineItem> outline_sub = new ArrayList<>();
            List<outlineItem> outline_obj = new ArrayList<>();
            Set<String> s_sub = new HashSet<>();
            Set<String> s_obj = new HashSet<>();
            for(int i=0;i<arr.length();i++){
                obj = arr.getJSONObject(i);
                String sub = obj.getString("subject");
                String verb = obj.getString("predicate");
                System.out.println(verb);
                String object = obj.getString("value");
                object = object.replaceAll("<br><br>","\n       ");
                object = object.replaceAll("<br>","\n       ");
                outlineItem item = new outlineItem(sub,verb,object);
                if(sub.equals(label)) {
                    outline_sub.add(item);
                    s_sub.add(verb);
                }
                else {
                    outline_obj.add(item);
                    s_obj.add(verb);
                }
            }
            /*for(String verb:s){
                outline_list.add(new outlineItem(verb,null,null));
                for(outlineItem item:outline_titles){
                    if(item.getVerb().equals(verb))
                        outline_list.add(item);
                }
            }*/
            if(!outline_sub.isEmpty()){
                outlineItem item = new outlineItem(null,null,null);
                item.setProperty("主属性");
                outline_list.add(item);
                for(String verb:s_sub){
                    outline_list.add(new outlineItem(verb,null,null));
                    for(outlineItem it:outline_sub){
                        if(it.getVerb().equals(verb))
                            outline_list.add(it);
                    }
                }
            }
            if(!outline_obj.isEmpty()){
                outlineItem item = new outlineItem(null,null,null);
                item.setProperty("从属性");
                outline_list.add(item);
                for(String verb:s_obj){
                    outline_list.add(new outlineItem(verb,null,null));
                    for(outlineItem it:outline_obj){
                        if(it.getVerb().equals(verb))
                            outline_list.add(it);
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void back_function(int id){
        switchToFunction();
    }
}