package com.fairychild.edukguser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnSignIn;
    private EditText etPhone;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnSignIn = findViewById(R.id.btn_sign_in);
        etPhone = findViewById(R.id.login_phone);
        etPassword = findViewById(R.id.login_password);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://47.93.101.225:8080/login";
                        String json = "{\n" +
                                " \"account\":\"" + etPhone.getText() + "\",\n" +
                                " \"password\":\"" + etPassword.getText() + "\"\n" +
                                "}";
                        try {
                            String response = OkHttp.post(url, json);
                            System.out.println(response);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}