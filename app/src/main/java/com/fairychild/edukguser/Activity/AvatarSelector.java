package com.fairychild.edukguser.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.fairychild.edukguser.R;

public class AvatarSelector extends AppCompatActivity{
    private Button back_btn;
    private RadioButton head_1;
    private RadioButton head_2;
    private RadioButton head_3;
    private RadioButton head_4;
    private RadioButton head_5;
    private RadioButton head_6;
    private RadioButton head_7;
    private RadioButton head_8;
    private RadioButton head_9;
    private String idx;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_selector);
        head_1=findViewById(R.id.head1_radio);
        head_2=findViewById(R.id.head2_radio);
        head_3=findViewById(R.id.head3_radio);
        head_4=findViewById(R.id.head4_radio);
        head_5=findViewById(R.id.head5_radio);
        head_6=findViewById(R.id.head6_radio);
        head_7=findViewById(R.id.head7_radio);
        head_8=findViewById(R.id.head8_radio);
        head_9=findViewById(R.id.head9_radio);
        head_9.setChecked(false);
        idx="9";
        head_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_2.setChecked(false);
                head_3.setChecked(false);
                head_4.setChecked(false);
                head_5.setChecked(false);
                head_6.setChecked(false);
                head_7.setChecked(false);
                head_8.setChecked(false);
                head_9.setChecked(false);
                idx="1";
            }
        });
        head_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_1.setChecked(false);
                head_3.setChecked(false);
                head_4.setChecked(false);
                head_5.setChecked(false);
                head_6.setChecked(false);
                head_7.setChecked(false);
                head_8.setChecked(false);
                head_9.setChecked(false);
                idx="2";
            }
        });
        head_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_2.setChecked(false);
                head_1.setChecked(false);
                head_4.setChecked(false);
                head_5.setChecked(false);
                head_6.setChecked(false);
                head_7.setChecked(false);
                head_8.setChecked(false);
                head_9.setChecked(false);
                idx="3";
            }
        });
        head_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_2.setChecked(false);
                head_3.setChecked(false);
                head_1.setChecked(false);
                head_5.setChecked(false);
                head_6.setChecked(false);
                head_7.setChecked(false);
                head_8.setChecked(false);
                head_9.setChecked(false);
                idx="4";
            }
        });
        head_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_2.setChecked(false);
                head_3.setChecked(false);
                head_4.setChecked(false);
                head_1.setChecked(false);
                head_6.setChecked(false);
                head_7.setChecked(false);
                head_8.setChecked(false);
                head_9.setChecked(false);
                idx="5";
            }
        });
        head_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_2.setChecked(false);
                head_3.setChecked(false);
                head_4.setChecked(false);
                head_5.setChecked(false);
                head_1.setChecked(false);
                head_7.setChecked(false);
                head_8.setChecked(false);
                head_9.setChecked(false);
                idx="6";
            }
        });
        head_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_2.setChecked(false);
                head_3.setChecked(false);
                head_4.setChecked(false);
                head_5.setChecked(false);
                head_6.setChecked(false);
                head_1.setChecked(false);
                head_8.setChecked(false);
                head_9.setChecked(false);
                idx="7";
            }
        });
        head_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_2.setChecked(false);
                head_3.setChecked(false);
                head_4.setChecked(false);
                head_5.setChecked(false);
                head_6.setChecked(false);
                head_7.setChecked(false);
                head_1.setChecked(false);
                head_9.setChecked(false);
                idx="8";
            }
        });
        head_9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                head_2.setChecked(false);
                head_3.setChecked(false);
                head_4.setChecked(false);
                head_5.setChecked(false);
                head_6.setChecked(false);
                head_7.setChecked(false);
                head_8.setChecked(false);
                head_1.setChecked(false);
                idx="9";
            }
        });
        back_btn=findViewById(R.id.avatar_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                intent.putExtra("idx",idx);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }
}
