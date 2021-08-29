package com.fairychild.edukguser.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fairychild.edukguser.OkHttp;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Knowledge;
import com.fairychild.edukguser.fragment.SearchResultListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Spinner subjectSpinner;
    private String subject;

    private EditText searchContent;

    private Button backBtn;
    private Button searchBtn;

    private FragmentTransaction transaction;
    private FragmentManager mSupportFragmentManager;
    private Fragment currentFragment;

    private boolean firstInit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        subjectSpinner = findViewById(R.id.subject_spinner);
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

        searchContent = findViewById(R.id.search_content);

        searchBtn = findViewById(R.id.real_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = searchContent.getText().toString();
                System.out.println(content);
                String id = getSharedPreferences("data", MODE_PRIVATE).getString("id", null);
                if (!content.equals("") && subject != null && id != null) {
                        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList"
                                + "?course=" + subject
                                + "&searchKey=" + content
                                + "&id=" + id;
                        System.out.println(url);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String response = OkHttp.get(url);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SearchActivity.this, "搜索成功", Toast.LENGTH_SHORT).show();
                                            System.out.println(response);
                                            showFragment(response);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SearchActivity.this, "发送问题失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                } else if (content.equals("")) {
                    Toast.makeText(SearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                } else if (id == null) {
                    Toast.makeText(SearchActivity.this, "您还没有登录，请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchActivity.this, "请选择学科", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSupportFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
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
            SearchResultListFragment searchResultListFragment = SearchResultListFragment.newInstance(content.size(), content);
            transaction.add(R.id.search_frame_layout, searchResultListFragment);
            if (!firstInit) {
                transaction.remove(currentFragment);
            }
            firstInit = false;
            transaction.show(searchResultListFragment);
            currentFragment = searchResultListFragment;
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("showFragment wrong!!!!!!!!!!");
        }
        transaction.commit();
    }
}